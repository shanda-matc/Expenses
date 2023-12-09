package edu.shanda.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.shanda.auth.*;
import edu.shanda.entity.User;
import edu.shanda.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.*;
import javax.servlet.http.HttpSession;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(
        urlPatterns = {"/auth"}
)

/*
  Inspired by: https://stackoverflow.com/questions/52144721/how-to-get-access-token-using-client-credentials-using-java-code
 */
public class Auth extends HttpServlet implements PropertiesLoader {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String REDIRECT_URL;
    private String OAUTH_URL;
    private String REGION;
    private String POOL_ID;
    Keys jwks;
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            loadKey();
        } catch (Exception e) {
            logger.error("Error during servlet initialization: " + e.getMessage(), e);
        }
        CLIENT_ID = (String) getServletContext().getAttribute("CLIENT_ID");
        CLIENT_SECRET = (String) getServletContext().getAttribute("CLIENT_SECRET");
        REDIRECT_URL = (String) getServletContext().getAttribute("REDIRECT_URL");
        OAUTH_URL = (String) getServletContext().getAttribute("OAUTH_URL");
        REGION = (String) getServletContext().getAttribute("REGION");
        POOL_ID = (String) getServletContext().getAttribute("POOL_ID");
        loadKey();

        System.out.println("in nit redirect" + REDIRECT_URL);
        logger.debug("In first One: " + REDIRECT_URL);

    }

    /**
     * Gets the auth code from the request and exchanges it for a token containing user info.
     * @param req servlet request
     * @param resp servlet response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Entering doGet method");
        String authCode = req.getParameter("code");
        logger.debug("Auth code: " + authCode);
        String userName = null;

        System.out.println("auth code:" + authCode);
        // Get the session
        HttpSession session = req.getSession();

        if (authCode == null) {
            //forward to an error page or back to the login
            RequestDispatcher errorDispatcher = req.getRequestDispatcher("errorPage.jsp");
            errorDispatcher.forward(req,resp);
        } else {
            HttpRequest authRequest = buildAuthRequest(authCode);
            try {
                TokenResponse tokenResponse = getToken(authRequest);
                userName = validate(tokenResponse, session);

                // lSet the username in the session
                session.setAttribute("userName", userName);

                // lSet the username attribute in the request to be used in the JSP
                req.setAttribute("userName", userName);

                // lAdd a flag to indicate successful sign-in
                req.setAttribute("signInSuccess", true);
            } catch (IOException e) {
                logger.error("Error getting or validating the token: " + e.getMessage(), e);
                RequestDispatcher errorDispatcher = req.getRequestDispatcher("errorPage.jsp");
                errorDispatcher.forward(req,resp);
                return;
            } catch (InterruptedException e) {
                logger.error("Error getting token from Cognito oauth url " + e.getMessage(), e);
                RequestDispatcher errorDispatcher = req.getRequestDispatcher("errorPage.jsp");
                errorDispatcher.forward(req,resp);
                return;
            }
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
        dispatcher.forward(req, resp);
    }

    /**
     * Sends the request for a token to Cognito and maps the response
     * @param authRequest the request to the oauth2/token url in cognito
     * @return response from the oauth2/token endpoint which should include id token, access token and refresh token
     * @throws IOException
     * @throws InterruptedException
     */
    private TokenResponse getToken(HttpRequest authRequest) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<?> response = null;

        response = client.send(authRequest, HttpResponse.BodyHandlers.ofString());

        logger.debug("Response headers: " + response.headers().toString());
        logger.debug("Response body: " + response.body().toString());

        ObjectMapper mapper = new ObjectMapper();
        TokenResponse tokenResponse = mapper.readValue(response.body().toString(), TokenResponse.class);
        logger.debug("Id token: " + tokenResponse.getIdToken());

        return tokenResponse;
    }

    /**
     * Get values out of the header to verify the token is legit. If it is legit, get the claims from it, such
     * as username.
     * @param tokenResponse
     * @return
     * @throws IOException
     */
    private String validate(TokenResponse tokenResponse, HttpSession session) throws IOException {
        logger.debug("Entering validate method");
        ObjectMapper mapper = new ObjectMapper();
        CognitoTokenHeader tokenHeader = mapper.readValue(CognitoJWTParser.getHeader(tokenResponse.getIdToken()).toString(), CognitoTokenHeader.class);

        // Header should have kid and alg- https://docs.aws.amazon.com/cognito/latest/developerguide/amazon-cognito-user-pools-using-the-id-token.html
        String keyId = tokenHeader.getKid();
        String alg = tokenHeader.getAlg();

        // Use Key's N and E
        BigInteger modulus = new BigInteger(1, org.apache.commons.codec.binary.Base64.decodeBase64(jwks.getKeys().get(0).getN()));
        BigInteger exponent = new BigInteger(1, org.apache.commons.codec.binary.Base64.decodeBase64(jwks.getKeys().get(0).getE()));

        // Create a public key
        PublicKey publicKey = null;
        try {
            publicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, exponent));
        } catch (InvalidKeySpecException e) {
            logger.error("Invalid Key Error " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Algorithm Error " + e.getMessage(), e);
        }

        // get an algorithm instance
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);

        // Verify ISS field of the token to make sure it's from the Cognito source
        String iss = String.format("https://cognito-idp.%s.amazonaws.com/%s", REGION, POOL_ID);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(iss)
                .withClaim("token_use", "id") // make sure you're verifying id token
                .build();

        // Verify the token
        DecodedJWT jwt = verifier.verify(tokenResponse.getIdToken());
        String userName = jwt.getClaim("cognito:username").asString();
        String email = jwt.getClaim("email").asString(); // lextracting email

        logger.debug("here's the username: " + userName);
        logger.debug("here are all the available claims: " + jwt.getClaims());

        // Set the attributes in the session
        session.setAttribute("userName", userName);
        session.setAttribute("userEmail", email);

        // decide what you want to do with the info!
        // for now, I'm just returning username for display back to the browser
        // Create a User object
        User user = new User();
        user.setUsername(userName);
        user.setEmail(email);

        return userName;
    }

    /** Create the auth url and use it to build the request.
     *
     * @param authCode auth code received from Cognito as part of the login process
     * @return the constructed oauth request
     */
    private HttpRequest buildAuthRequest(String authCode) {
        if (CLIENT_ID == null || CLIENT_SECRET == null || authCode == null || REDIRECT_URL == null) {
            return null;
        }
        String keys = CLIENT_ID + ":" + CLIENT_SECRET;

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", "authorization_code");
        parameters.put("client-secret", CLIENT_SECRET);
        parameters.put("client_id", CLIENT_ID);
        parameters.put("code", authCode);
        parameters.put("redirect_uri", REDIRECT_URL);


        String form = parameters.keySet().stream()
                .map(key -> key + "=" + URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String encoding = Base64.getEncoder().encodeToString(keys.getBytes());

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(OAUTH_URL))
                .headers("Content-Type", "application/x-www-form-urlencoded", "Authorization", "Basic " + encoding)
                .POST(HttpRequest.BodyPublishers.ofString(form)).build();
        System.out.println("Redirct:" + REDIRECT_URL);
        logger.info("Number-2:" + REDIRECT_URL);
        return request;

    }

    /**
     * Gets the JSON Web Key Set (JWKS) for the user pool from cognito and loads it
     * into objects for easier use.
     *
     * JSON Web Key Set (JWKS) location: https://cognito-idp.{region}.amazonaws.com/{userPoolId}/.well-known/jwks.json
     * Demo url: https://cognito-idp.us-east-2.amazonaws.com/us-east-2_XaRYHsmKB/.well-known/jwks.json
     *
     * @see Keys
     * @see KeysItem
     */
    private void loadKey() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            URL jwksURL = new URL(String.format("https://cognito-idp.%s.amazonaws.com/%s/.well-known/jwks.json", REGION, POOL_ID));
            File jwksFile = new File("jwks.json");
            FileUtils.copyURLToFile(jwksURL, jwksFile);
            jwks = mapper.readValue(jwksFile, Keys.class);
            logger.debug("Keys are loaded. Here's e: " + jwks.getKeys().get(0).getE());
        } catch (IOException ioException) {
            logger.error("Cannot load json..." + ioException.getMessage(), ioException);
        } catch (Exception e) {
            logger.error("Error loading json" + e.getMessage(), e);
        }
    }
}

