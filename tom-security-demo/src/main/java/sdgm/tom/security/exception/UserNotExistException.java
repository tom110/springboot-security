package sdgm.tom.security.exception;

public class UserNotExistException extends RuntimeException {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserNotExistException(String message,String id) {
        super(message);
        this.id=id;
    }
}
