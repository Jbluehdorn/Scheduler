package jbluehdorn.Scheduler;

import javafx.application.Application;
import javafx.stage.Stage;
import jbluehdorn.Scheduler.repositories.UserRepository;
import jbluehdorn.Scheduler.view.FxmlView;
import jbluehdorn.Scheduler.view.StageManager;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SchedulerApplication extends Application {
    protected ConfigurableApplicationContext springContext;
    protected StageManager stageManager;
    

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = bootstrapSpringApplicationContext();
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        stageManager = springContext.getBean(StageManager.class, primaryStage);
        displayInitialScene();
    }
    
    @Override
    public void stop() throws Exception {
        springContext.close();
    }
    
    protected void displayInitialScene() {
//        stageManager.switchScene(FxmlView.LOGIN);
        try {
            UserRepository.validateCredentials("admin", "password");
            stageManager.switchScene(FxmlView.APPOINTMENT_FORM);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private ConfigurableApplicationContext bootstrapSpringApplicationContext() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(SchedulerApplication.class);
        
        String[] args = getParameters().getRaw().stream().toArray(String[]::new);
        builder.headless(false);
        
        return builder.run(args);
    }
}
