package validate;

public interface Validator<T> {
    /***
     * validation function
     * @param entity - T
     * @throws ValidationException
     * */
    void validate(T entity) throws  ValidationException;
}


