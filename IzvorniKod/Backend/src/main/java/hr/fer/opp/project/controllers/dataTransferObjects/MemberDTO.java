package hr.fer.opp.project.controllers.dataTransferObjects;

public class MemberDTO {

    private Long id;
    private String username;



    /**
     * Gets id.
     *
     * @return Value of id.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets username.
     *
     * @return Value of username.
     */
    public String getUsername() {
        return username;
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
     * Sets new id.
     *
     * @param id New value of id.
     */
    public void setId(long id) {
        this.id = id;
    }
}
