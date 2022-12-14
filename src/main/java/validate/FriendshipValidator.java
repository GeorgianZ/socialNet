package validate;

import domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {


    /***
     * validate a frienship
     * @param entity - Friendship
     * @throws ValidationException
     * */
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getUser1() == entity.getUser2());
    }
}
