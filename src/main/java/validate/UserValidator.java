package validate;

import domain.User;

public class UserValidator implements Validator<User>{

    /***
     * validate an User
     * @param entity - User
     * @throws ValidationException
     * */
    @Override
    public void validate(User entity) throws ValidationException {
        if(entity.getFirstName().isEmpty() || entity.getLastName().isEmpty());
    }
}


