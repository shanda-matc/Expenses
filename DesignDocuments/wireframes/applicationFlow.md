## User Signup
- User can choose Signup/SignIn on the menu form the index page

## Read
- user can choose `Read` on the menu to read all the stories
- Page sends a request to displayStories Servlet
- On 'Read' page, user can leave a comment on a story, if authenticated
- If user is no-authenticated, the message will pop up to Sign In and redirect to the login page
- There are different `categories` on which user can click and read the story
- On each `category` there is a comment box to leave comment for authenticated user

## Write
- User can choose `Write` on the menu to write a story
- When user will click on the `submit` button it will check if user is authenticated, otherwise a popup messAage will display and redirect ot the `login` page
