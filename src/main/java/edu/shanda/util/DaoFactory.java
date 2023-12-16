package edu.shanda.util;

import edu.shanda.persistence.GenericDao;

/**
 * The type Dao factory.
 */
public class DaoFactory {
    private DaoFactory() {
    }

    /**
     * Create dao generic dao.
     *
     * @param type the type
     * @return the generic dao
     */
    public static GenericDao createDao(Class type) {
        return new GenericDao(type);
    }
}
