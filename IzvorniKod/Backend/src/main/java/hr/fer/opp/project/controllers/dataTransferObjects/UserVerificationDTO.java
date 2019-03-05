package hr.fer.opp.project.controllers.dataTransferObjects;

public class UserVerificationDTO {

    private String username;
    private String code;

    /**
     * Gets username.
     *
     * @return Value of username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets code.
     *
     * @return Value of code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets new username.
     *
     * @param username New value of username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets new code.
     *
     * @param code New value of code.
     */
    public void setCode(String code) {
        this.code = code;
    }
}
