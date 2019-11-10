package ds;

import java.sql.*;
import java.util.Date;
import java.io.Serializable;
import java.util.ArrayList;

public class Manager implements Serializable {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Project> projects = new ArrayList<>();
    private int numberOfTasks = 0;
    private User online = null;

    Connection connDB = null;

    public void connectDB() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String DB_URL = "jdbc:mysql://localhost/todolist";
        String DB_USER = "root";
        String DB_PASS = "root";
        connDB = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public void disconnectDB() throws Exception{
        connDB.close();
    }

    public Person registerPerson(String login, String pass, String firstName, String lastName, String email) throws Exception {
        if (getUserByLogin(login) == null) {
            Person newPerson = new Person(0, login, pass, firstName, lastName, email);
            int userID = 0;

            connectDB();
            PreparedStatement ps = connDB.prepareStatement("INSERT INTO users (user_login, user_pass, user_active, user_email, user_type)"
                    + "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newPerson.getLogin());
            ps.setString(2, newPerson.getPass());
            ps.setBoolean(3, newPerson.isActive());
            ps.setString(4, newPerson.getEmail());
            ps.setString(5, newPerson.getUserType());
            System.out.println(newPerson.getUserType());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
                userID = rs.getInt(1);

            ps = connDB.prepareStatement("INSERT INTO persons (person_firstname, person_lastname, user_id)" + "VALUES(?, ?, ?)");
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setInt(3, userID);
            ps.executeUpdate();

            rs.close();
            ps.close();
            disconnectDB();

            //users.add(newPerson);
            return newPerson;
        }
        return null;
    }

    public Company registerCompany(String login, String pass, String title, String email) throws Exception {
        if (getUserByLogin(login) == null) {
            Company newCompany = new Company(0, login, pass, title, email);
            int userID = 0;

            connectDB();
            PreparedStatement ps = connDB.prepareStatement("INSERT INTO users (user_login, user_pass, user_active, user_email, user_type)"
                    + "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newCompany.getLogin());
            ps.setString(2, newCompany.getPass());
            ps.setBoolean(3, newCompany.isActive());
            ps.setString(4, newCompany.getEmail());
            ps.setString(5, newCompany.getUserType());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
                userID = rs.getInt(1);

            ps = connDB.prepareStatement("INSERT INTO companies (company_title, user_id)" + "VALUES(?, ?)");
            ps.setString(1, title);
            ps.setInt(2, userID);
            ps.executeUpdate();

            rs.close();
            ps.close();
            disconnectDB();

            //users.add(newCompany);
            return newCompany;
        }
        return null;
    }

    public ArrayList<User> getAllUsers() throws Exception {
        ArrayList<User> users = new ArrayList<>();
        connectDB();
        Statement stmt = connDB.createStatement();
        String sqlQuery = "SELECT * FROM users u LEFT JOIN persons p ON u.user_id=p.user_id LEFT JOIN companies c ON u.user_id=c.user_id";
        ResultSet rs = stmt.executeQuery(sqlQuery);

        User user = null;
        while(rs.next()){
            int id = rs.getInt("user_id");
            String login = rs.getString("user_login");
            String pass = rs.getString("user_pass");
            String email = rs.getString("user_email");
            String userType = rs.getString("user_type");
            boolean active = rs.getBoolean("user_active");

            if(userType.equals("Person")){
                String firstName = rs.getString("person_firstname");
                String lastName = rs.getString("person_lastname");
                Person person = new Person(id, login, pass, firstName, lastName, email);
                person.setActive(active);
                person.setUserType(userType);
                user = person;
            }
            else {
                String title = rs.getString("company_title");
                Company company = new Company(id, login, pass, title, email);
                company.setActive(active);
                company.setUserType(userType);
                user = company;
            }
            users.add(user);
        }

        rs.close();
        stmt.close();
        disconnectDB();
        return users;
    }

    public ArrayList<User> getAllActiveUsers() throws Exception {
        ArrayList<User> activeUsers = new ArrayList<>();
        connectDB();
        Statement stmt = connDB.createStatement();
        String sqlQuery = "SELECT * FROM users u LEFT JOIN persons p ON u.user_id=p.user_id LEFT JOIN companies c ON u.user_id=c.user_id WHERE u.user_active = 1";
        ResultSet rs = stmt.executeQuery(sqlQuery);

        User user = null;
        while(rs.next()){
            int id = rs.getInt("user_id");
            String login = rs.getString("user_login");
            String pass = rs.getString("user_pass");
            String email = rs.getString("user_email");
            String userType = rs.getString("user_type");
            boolean active = rs.getBoolean("user_active");

            if(userType.equals("Person")){
                String firstName = rs.getString("person_firstname");
                String lastName = rs.getString("person_lastname");
                Person person = new Person(id, login, pass, firstName, lastName, email);
                person.setActive(active);
                person.setUserType(userType);
                user = person;
            }
            else {
                String title = rs.getString("company_title");
                Company company = new Company(id, login, pass, title, email);
                company.setActive(active);
                company.setUserType(userType);
                user = company;
            }
            activeUsers.add(user);
        }

        rs.close();
        stmt.close();
        disconnectDB();
        return activeUsers;
    }

    public User getUserById(int userId) throws Exception {
        if(online != null){
            connectDB();
            PreparedStatement ps = connDB.prepareStatement("SELECT * FROM users u LEFT JOIN persons p ON u.user_id=p.user_id LEFT JOIN companies c ON u.user_id=c.user_id WHERE u.user_id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            User user = null;

            while(rs.next()){
                int id = rs.getInt("user_id");
                String login = rs.getString("user_login");
                String pass = rs.getString("user_pass");
                String email = rs.getString("user_email");
                String userType = rs.getString("user_type");
                boolean active = rs.getBoolean("user_active");

                if(userType.equals("Person")){
                    String firstName = rs.getString("person_firstname");
                    String lastName = rs.getString("person_lastname");
                    Person person = new Person(id, login, pass, firstName, lastName, email);
                    person.setActive(active);
                    person.setUserType(userType);
                    user = person;
                }
                else {
                    String title = rs.getString("company_title");
                    Company company = new Company(id, login, pass, title, email);
                    company.setActive(active);
                    company.setUserType(userType);
                    user = company;
                }
            }

            rs.close();
            ps.close();
            disconnectDB();
            return user;
        }
//        if (online != null)
//            for (User user : users)
//                if (user.getId() == userId)
//                    return user;
        return null;
    }

    public User getUserByLogin(String userLogin) throws Exception {
        if(online != null){
            connectDB();
            PreparedStatement ps = connDB.prepareStatement("SELECT * FROM users u LEFT JOIN persons p ON u.user_id=p.user_id LEFT JOIN companies c ON u.user_id=c.user_id WHERE u.user_login = ?");
            ps.setString(1, userLogin);
            ResultSet rs = ps.executeQuery();
            User user = null;
            while(rs.next()){
                int id = rs.getInt("user_id");
                String login = rs.getString("user_login");
                String pass = rs.getString("user_pass");
                String email = rs.getString("user_email");
                String userType = rs.getString("user_type");
                boolean active = rs.getBoolean("user_active");

                if(userType.equals("Person")){
                    String firstName = rs.getString("person_firstname");
                    String lastName = rs.getString("person_lastname");
                    Person person = new Person(id, login, pass, firstName, lastName, email);
                    person.setActive(active);
                    person.setUserType(userType);
                    user = person;
                }
                else {
                    String title = rs.getString("company_title");
                    Company company = new Company(id, login, pass, title, email);
                    company.setActive(active);
                    company.setUserType(userType);
                    user = company;
                }
            }

            rs.close();
            ps.close();
            disconnectDB();
            return user;
        }
//        if (online != null)
//            for (User user : users)
//                if (user.getLogin().equals(login))
//                    return user;
        return null;
    }

    public Project getProjectById(int projectId) throws Exception {
        if(online != null){
            connectDB();
            PreparedStatement ps = connDB.prepareStatement("SELECT * FROM projects WHERE project_id = ?");
            ps.setInt(1, projectId);
            ResultSet rs = ps.executeQuery();
            Project project = null;
            int id = 0;
            while(rs.next()){
                id = rs.getInt("project_id");
                String title = rs.getString("project_title");
                project = new Project(id, title);
            }

            rs.close();
            ps.close();
            disconnectDB();

            return project;
        }
//        if (online != null)
//            for (Project project : projects)
//                if (project.getId() == projectId)
//                    return project;
        return null;
    }

    public User login(String login, String pass) throws Exception {
        for (User user : getAllUsers()) {
            if (user.getLogin().equals(login) && user.getPass().equals(pass) && user.isActive()) {
                online = user;
                return user;
            }
        }
        throw new ObjectDoesNotExist("Incorrect login data.");
    }

    public void logout() {
        online = null;
    }

    public void toggleUserActive(int userId, boolean isActive) throws Exception {
        User currentUser = getUserById(userId);
        if (currentUser == null) throw new ObjectDoesNotExist("User with ID: [" + userId + "] does not exist.");
        currentUser.setActive(isActive);

        connectDB();
        PreparedStatement ps = connDB.prepareStatement("UPDATE users SET user_active = ? WHERE user_id = ?");
        ps.setBoolean(1, currentUser.isActive());
        ps.setInt(2, currentUser.getId());
        ps.executeUpdate();
        ps.close();
        disconnectDB();
    }

    public boolean editPersonInfo(int userId, String firstName, String lastName, String email) throws Exception {
        User currentUser = getUserById(userId);
        if (currentUser == null) throw new ObjectDoesNotExist("User with ID: [" + userId + "] does not exist.");
        if (currentUser.getClass().equals(Person.class)) {
            Person person = (Person) currentUser;

            connectDB();
            PreparedStatement ps = connDB.prepareStatement("UPDATE persons SET person_firstname = ?, person_lastname = ? WHERE user_id = ?");
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setInt(3, person.getId());
            ps.executeUpdate();

            ps = connDB.prepareStatement("UPDATE users SET user_email = ? WHERE user_id = ?");
            ps.setString(1, email);
            ps.setInt(2, person.getId());
            ps.executeUpdate();

            ps.close();
            disconnectDB();

            return true;
        }
        throw new Exception("Type of user " + currentUser.getClass() + " does not match required type [Person].");
    }

    public boolean editCompanyInfo(int userId, String title, String email) throws Exception {
        User currentUser = getUserById(userId);
        if (currentUser == null) throw new ObjectDoesNotExist("User with ID: [" + userId + "] does not exist.");
        if (currentUser.getClass().equals(Company.class)) {
            Company company = (Company) currentUser;

            connectDB();
            PreparedStatement ps = connDB.prepareStatement("UPDATE companies SET company_title = ? WHERE user_id = ?");
            ps.setString(1, title);
            ps.setInt(2, company.getId());
            ps.executeUpdate();

            ps = connDB.prepareStatement("UPDATE users SET user_email = ? WHERE user_id = ?");
            ps.setString(1, email);
            ps.setInt(2, company.getId());
            ps.executeUpdate();

            ps.close();
            disconnectDB();

            return true;
        }
        throw new Exception("Type of user " + currentUser.getClass() + " does not match required type [Company].");
    }

    public Project editProjectInfo(int projectId, String projectTitle) throws Exception {
        Project project = getProjectById(projectId);
        if (project != null && projectTitle.length() > 5) {
            connectDB();
            PreparedStatement ps = connDB.prepareStatement("UPDATE projects SET project_title = ? WHERE project_id = ?");
            ps.setString(1, projectTitle);
            ps.setInt(2, project.getId());
            ps.executeUpdate();

            ps.close();
            disconnectDB();

            return project;
        }
        throw new ObjectDoesNotExist("Project with ID: [" + projectId + "] does not exist.");
    }

    public boolean editTaskInfo(int taskId, String taskTitle) throws Exception {
        Task task = getUserTaskById(taskId);
        if (task == null) throw new ObjectDoesNotExist("Task with ID: " + taskId + " does not exist.");
        if (task.isDone())
            throw new Exception("Task with ID: " + taskId + " cannot be edited because it is completed.");
        if (taskTitle.length() > 5) {
            connectDB();
            PreparedStatement ps = connDB.prepareStatement("UPDATE tasks SET task_title = ?, task_edited_on = ?, task_edited_by = ? WHERE task_id = ?");
            ps.setString(1, taskTitle);
            ps.setDate(2, (java.sql.Date) new Date());
            ps.setString(3, online.toString());
            ps.setInt(4, task.getId());
            ps.executeUpdate();

            ps.close();
            disconnectDB();

            return true;
        }
        return false;
    }

    public Project addProject(String title) throws Exception {
        if (online != null && title.length() > 4) {
            Project newProject = new Project(0, title, online);
            int projectID = 0;

            connectDB();
            PreparedStatement ps = connDB.prepareStatement("INSERT INTO projects (project_title)"
                                                                + "VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newProject.getTitle());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
                projectID = rs.getInt(1);

            rs.close();
            ps.close();
            disconnectDB();
//            projects.add(newProject);
//            online.addProject(newProject);
            newProject.setId(projectID);
            return newProject;
        }
        return null;
    }

    public Project addProject(String title, int creatorId) throws Exception {
        User creator = getUserById(creatorId);
        if (creator != null && creator.isActive() && title.length() > 4) {
            Project newProject = new Project(0, title, creator);
            int projectID = 0;

            connectDB();
            PreparedStatement ps = connDB.prepareStatement("INSERT INTO projects (project_title)"
                                                                + "VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newProject.getTitle());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
                projectID = rs.getInt(1);

            rs.close();
            ps.close();
            disconnectDB();
//            projects.add(newProject);
//            creator.addProject(newProject);
            newProject.setId(projectID);
            return newProject;
        }
        return null;
    }

    public boolean deleteProject(int projectId) throws Exception {
        Project projectToDelete = getProjectById(projectId);
        if (projectToDelete != null) {
            for (User member : projectToDelete.getMembers())
                member.removeProject(projectToDelete);
            projects.remove(projectToDelete);
            return true;
        }
        throw new ObjectDoesNotExist("Project with ID: [" + projectId + "] does not exist.");
    }

    public ArrayList<Project> getAllUserProjects() throws Exception {
        if (online != null){
            ArrayList<Project> userProjects = new ArrayList<>();

            connectDB();
            PreparedStatement ps = connDB.prepareStatement("SELECT p.* FROM projects p, user_projects up WHERE p.project_id = up.project_id AND up.user_id = ?");
            ps.setInt(1, online.getId());
            ResultSet rs = ps.executeQuery();
            Project project = null;

            while(rs.next()){
                int id = rs.getInt("project_id");
                String title = rs.getString("project_title");
                project = new Project(id, title);
                userProjects.add(project);
            }

            rs.close();
            ps.close();
            disconnectDB();
            return userProjects;
            //return online.getProjects();
        }
        return new ArrayList<>();
    }

    public Project getUserProjectById(int projectId) throws Exception {
        if(online != null){
            connectDB();
            PreparedStatement ps = connDB.prepareStatement("SELECT p.* FROM projects p, user_projects up WHERE p.project_id = up.project_id AND up.user_id = ? AND p.project_id = ?");
            ps.setInt(1, online.getId());
            ps.setInt(2, projectId);
            ResultSet rs = ps.executeQuery();
            Project project = null;

            while(rs.next()){
                int id = rs.getInt("project_id");
                String title = rs.getString("project_title");
                project = new Project(id, title);
            }

            rs.close();
            ps.close();
            disconnectDB();
            return project;
        }
//        if (online != null)
//            for (Project project : online.getProjects())
//                if (project.getId() == projectId)
//                    return project;
        return null;
    }

    public Task getUserTaskById(int taskId) {
        if (online != null)
            for (Project project : online.getProjects()) {
                ArrayList<Task> allTasks = project.getAllTasks();
                for (Task task : allTasks)
                    if (task.getId() == taskId)
                        return task;
            }
        return null;
    }

    public Task getTaskById(int taskId) {
        if (online != null)
            for (User user : this.users)
                for (Project project : user.getProjects()) {
                    ArrayList<Task> allTasks = project.getAllTasks();
                    for (Task task : allTasks)
                        if (task.getId() == taskId)
                            return task;
                }
        return null;
    }

    public boolean addProjectMember(int projectId, int userId) throws Exception {
        User newMember = getUserById(userId);
        Project project = getProjectById(projectId);
        if (newMember == null) throw new ObjectDoesNotExist("User with ID: [" + userId + "] does not exist.");
        if (project == null) throw new ObjectDoesNotExist("Project with ID: [" + projectId + "] does not exist.");
        if (newMember.isActive()) {
            connectDB();
            PreparedStatement ps = connDB.prepareStatement("INSERT INTO user_projects (user_id, project_id)" + "VALUES(?, ?)");
            ps.setInt(1, userId);
            ps.setInt(2, projectId);
            ps.executeUpdate();

            ps.close();
            disconnectDB();

//            project.addMember(newMember);
//            newMember.addProject(project);
            return true;
        }
        throw new Exception("User with ID: [" + userId + "] is not activated.");
    }

    public ArrayList<User> getProjectMembers(int projectId) throws Exception {
        Project project = getProjectById(projectId);
        if(project != null){
            ArrayList<User> members = new ArrayList<>();
            connectDB();
            PreparedStatement ps = connDB.prepareStatement("SELECT * FROM user_projects up, " +
                    "(SELECT u.user_id, user_login, user_pass, user_email, user_active, user_type, person_firstname, person_lastname, company_title " +
                    "FROM users u " +
                    "LEFT JOIN persons p ON u.user_id=p.user_id " +
                    "LEFT JOIN companies c ON u.user_id=c.user_id) as users " +
                    "WHERE users.user_id = up.user_id AND up.project_id = ?");
            ps.setInt(1, projectId);

            ResultSet rs = ps.executeQuery();

            User user = null;
            while(rs.next()){
                int id = rs.getInt("user_id");
                String login = rs.getString("user_login");
                String pass = rs.getString("user_pass");
                String email = rs.getString("user_email");
                String userType = rs.getString("user_type");
                boolean active = rs.getBoolean("user_active");

                if(userType.equals("Person")){
                    String firstName = rs.getString("person_firstname");
                    String lastName = rs.getString("person_lastname");
                    Person person = new Person(id, login, pass, firstName, lastName, email);
                    person.setActive(active);
                    person.setUserType(userType);
                    user = person;
                }
                else {
                    String title = rs.getString("company_title");
                    Company company = new Company(id, login, pass, title, email);
                    company.setActive(active);
                    company.setUserType(userType);
                    user = company;
                }
                members.add(user);
            }
            rs.close();
            ps.close();
            disconnectDB();
            return members;
        }
//        if (project != null)
//            return project.getMembers();
        throw new ObjectDoesNotExist("Project with ID: [" + projectId + "] does not exist.");
    }

    public ArrayList<Project> getAllProjects() throws Exception {
        if(online != null){
            connectDB();
            Statement stmt = connDB.createStatement();
            String sqlQuery = "SELECT * FROM projects";
            ResultSet rs = stmt.executeQuery(sqlQuery);

            ArrayList<Project> projectsFromDB = new ArrayList<>();
            while(rs.next()){
                int id = rs.getInt("project_id");
                String title = rs.getString("project_title");
                Project project = new Project(id, title);
                projectsFromDB.add(project);
            }

            rs.close();
            stmt.close();
            disconnectDB();
            return projectsFromDB;
        }
//        if (online != null)
//            return projects;
        return new ArrayList<>();
    }

    public ArrayList<Project> getP() throws Exception {
        if(online != null){
            ArrayList<Project> projects = getAllProjects();
            for(Project project : projects){
                int projectID = project.getId();
                project.setMembers(getProjectMembers(projectID));
            }
            return projects;
        }
        return new ArrayList<>();
    }

    public Task addTaskToProject(int projectId, String taskTitle) throws Exception {
        Project project = getUserProjectById(projectId);
        if (project != null) {
            Task newTask = new Task(taskTitle, project, online);
            project.addTask(newTask);
            numberOfTasks++;
            return newTask;
        }
        throw new ObjectDoesNotExist("Project with ID: [" + projectId + "] does not exist in current user's projects list.");
    }

    public Task addTaskToProject(int projectId, String taskTitle, int creatorId) throws Exception {
        Project project = getProjectById(projectId);
        User creator = getUserById(creatorId);
        if (project != null && creator != null) {
            Task newTask = new Task(taskTitle, project, creator);
            project.addTask(newTask);
            numberOfTasks++;
            return newTask;
        }
        throw new ObjectDoesNotExist("Project with ID: [" + projectId + "] does not exist in current user's projects list.");
    }

    public Task addTaskToTask(int taskId, String taskTitle) throws Exception {
        Task parentTask = getUserTaskById(taskId);
        if (parentTask != null) {
            Task newTask = new Task(taskTitle, parentTask.getProject(), online);
            parentTask.addTask(newTask);
            numberOfTasks++;
            return newTask;
        }
        throw new ObjectDoesNotExist("Task with ID: " + taskId + " does not exist in current user's tasks list.");
    }

    public Task addTaskToTask(int taskId, String taskTitle, int creatorId) throws Exception {
        Task parentTask = getTaskById(taskId);
        User creator = getUserById(creatorId);
        if (parentTask != null && creator != null) {
            Task newTask = new Task(taskTitle, parentTask.getProject(), creator);
            parentTask.addTask(newTask);
            numberOfTasks++;
            return newTask;
        }
        throw new ObjectDoesNotExist("Task with ID: " + taskId + " does not exist in current user's tasks list.");
    }

    public Task completeTask(int taskId) throws Exception {
        Task taskToComplete = getUserTaskById(taskId);
        if (taskToComplete == null) throw new ObjectDoesNotExist("Task with ID: [" + taskId + "] does not exist.");
        if (taskToComplete.isDone())
            throw new ObjectDoesNotExist("Task with ID: [" + taskId + "] is already completed.");

        taskToComplete.setCompletedOn(new Date());
        taskToComplete.setCompletedBy(online);
        taskToComplete.setDone(true);
        return taskToComplete;
    }

    public ArrayList<Task> getProjectTasks(int projectId) throws Exception {
        Project project = getProjectById(projectId);
        if (project != null)
            return project.getTasks();
        throw new ObjectDoesNotExist("Project with ID: [" + projectId + "] does not exist.");
    }

    public int[] getUsersCounts() throws Exception {
        int[] num = new int[2];
        for (User user : getAllUsers()) {
            if (user.getUserType().equals("Person"))
                num[0]++;
            else
                num[1]++;
        }
        return num;
    }

    public int[][] getProjectNumbers() throws Exception {
        int[][] data = new int[getAllProjects().size()][2];
        int id = 0;
        for (Project project : getAllProjects()) {
            data[id][0] = project.getId();
            data[id][1] = project.getAllTasks().size();
            id++;
        }
        return data;
    }

}
