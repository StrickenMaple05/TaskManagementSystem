import java.io.Serializable;

public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * task code
     */
    private String code = "";
    /**
     * task name
     */
    private String name = "";
    /**
     * task description
     */
    private String description = "";
    /**
     * task executor
     */
    private String executor = "";
    /**
     * task status
     */
    private String status = "";

    public Task() {}

    public Task(String code, String name, String description, String executor, String status) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.executor = executor;
        this.status = status;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExecutor(String executor) {
        if (!executor.equals("_")) {
            this.executor = executor;
        }
    }

    public void setStatus(String status) {
        if (!status.equals("_")) {
            this.status = status;
        }
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getExecutor() {
        return executor;
    }

    public String getStatus() {
        return status;
    }

    public String getInfo() {
        return  "==================================\n" +
                "name: " + name +
                "\ndescription: " + description +
                "\nexecutor: " + executor +
                "\nstatus: " + status +
                "\ncode: " + code +
                "\n==================================";
    }
}