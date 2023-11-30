
import agents.PortalAgent;

public class PortalUI {
    private static PortalUI singleton = null;
    PortalAgent portalAgentInstance;
    String patientEmail;
    String therapistEmail;
    String patientName;
    Integer appID;
    private PortalUI(PortalAgent portalAgentInstance)
    {
        this.portalAgentInstance = portalAgentInstance;
    }


    public static PortalUI createUI(PortalAgent portalAgentInstance)
    {
        if (singleton == null)
        {
            singleton = new PortalUI(portalAgentInstance);

        }
        return singleton;

    }
    public static PortalUI returnSingleton()
    {
        return singleton;
    }


    public void startUI()
    {
        PatientUI patientUIInstance = PatientUI.createUI();
        patientUIInstance.show();


    }

    public void showHome()
    {
        HomeUI home = HomeUI.createUI();
        home.setName(this.patientName);
        home.show();

    }

    public void requestRegister(String name,String email,String phone,String password)
    {
        portalAgentInstance.registerRequest(name,email,phone,password);
    }
    
    public void requestLoginUser(String email, String password)
    {
        this.patientEmail = email;
        portalAgentInstance.loginRequest(email, password);
    }

       public void loginConfirm(boolean loginConfirm, String name)
    {
        this.patientName = name;
        if (loginConfirm)
        {
            LoginUI loginUIInstance = LoginUI.createUI();
            loginUIInstance.showSuccessLogin();
            loginUIInstance.disposeFrame();
            showHome();
        }
        else {
            LoginUI loginUIInstance = LoginUI.createUI();
            loginUIInstance.showFailureMessage();
        }
    }

        public void registerConfirm(boolean confirm)
    {
        if (confirm)
        {
            RegisterUI registerUIInstance = RegisterUI.createUI();
            registerUIInstance.showSuccessRegister();
            registerUIInstance.disposeFrame();
            LoginUI loginUIInstance = LoginUI.createUI();
            loginUIInstance.show();

        }
        else {
            RegisterUI registerUIInstance = RegisterUI.createUI();
            registerUIInstance.showFailureMessage();
        }
    }





}