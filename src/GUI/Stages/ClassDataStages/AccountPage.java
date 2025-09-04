    package GUI.Stages.ClassDataStages;

    import Controller.Controller;
    import Entity.*;
    import GUI.Pane.*;
    import GUI.Buttons.*;
    import GUI.Stages.MyStage;
    import javafx.geometry.Insets;
    import javafx.scene.*;
    import javafx.scene.input.KeyCode;
    import javafx.scene.layout.*;
    import javafx.scene.paint.Color;
    import javafx.stage.*;

    public class AccountPage extends MyStage {
        Controller controller;
        BorderPane root;
        StackPane content;

        MyButton impostazioniButton;
        MyButton accountButton;
        MyButton corsiButton;
        MyButton calendarButton;
        MyButton clickedButton;

        VBox impostazioniPanel;
        VBox accountPanel;
        BorderPane accountCorsiPanel;
        CalendarioPanel calendarioPanel;

        Utente utente;

        public AccountPage(Controller controller){
            super(1050, 750, RootType.BORDERPANE);
            this.controller = controller;

            root = getRootBorderPane();
            content = new StackPane();

            content.setStyle("-fx-background-color: WHITE;");
        }

        public void initPage(Utente utente){
            this.utente = utente;

            accountPanel = createAccountPanel(utente);
            accountCorsiPanel = createAccountCorsiPanel();
            impostazioniPanel = new ImpostazioniPanel(controller);
            calendarioPanel = new CalendarioPanel(controller);
            calendarioPanel.initCalendario(utente);

            content.getChildren().addAll(accountCorsiPanel,calendarioPanel,accountPanel,impostazioniPanel);

            HBox topBar = createTopBar();
            root.setTop(topBar);
            root.setCenter(content);

            showOnlyPanel(accountCorsiPanel);
            setButtonAsActive(corsiButton);
        }


        private void initButton(MyButton button, Pane panel) {
            button.setToOutlineButton();
            button.setOnAction(e -> {
                setButtonAsActive(button);
                showOnlyPanel(panel);
            });

            button.setOnMouseExited(e -> {
                if (button != clickedButton) {
                    button.setOutlineNotClickedStyle();
                }
            });
        }

        private void setButtonAsActive(MyButton button) {
            clickedButton = button;

            if(clickedButton != accountButton) accountButton.setOutlineNotClickedStyle();
            else accountButton.setOutlineClickedStyle();

            if(clickedButton != corsiButton) corsiButton.setOutlineNotClickedStyle();
            else corsiButton.setOutlineClickedStyle();

            if(clickedButton != impostazioniButton) impostazioniButton.setOutlineNotClickedStyle();
            else impostazioniButton.setOutlineClickedStyle();

            if(clickedButton != calendarButton) calendarButton.setOutlineNotClickedStyle();
            else calendarButton.setOutlineClickedStyle();

        }

        private VBox createAccountPanel(Utente utente){
            accountPanel = new AccountPanel(utente);
            accountPanel.setSpacing(10);
            accountPanel.setPadding(new Insets(10));
            return accountPanel;
        }

        private ElencoCorsiPanel createAccountCorsiPanel(){
            ElencoCorsiPanel elencoCorsiPanel = new ElencoCorsiPanel(this.controller);
            elencoCorsiPanel.initPanel(utente);
            elencoCorsiPanel.setVisible(true);
            elencoCorsiPanel.setManaged(true);
            return elencoCorsiPanel;
        }


        private HBox createTopBar() {
            HBox topBar = new HBox();
            topBar.setPadding(new Insets(10));
            topBar.setSpacing(10);
            topBar.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            topBar.setBorder(new Border(new BorderStroke(
                    Color.valueOf("#3A6698"),
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    new BorderWidths(0,0,2, 0)
            )));

            accountButton = createAccountButton();
            corsiButton = createCorsiButton();
            calendarButton = createCalendarButton();
            impostazioniButton = createImpostazioniButton();

            CircleButton minimizeBtn = new CircleButton().setToMinimizeButtonWithAction(this);
            CircleButton closeBtn = new CircleButton().setToCloseButtonWithAction(this);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            topBar.getChildren().addAll(corsiButton, calendarButton, accountButton, impostazioniButton, spacer, minimizeBtn, closeBtn);

            return  topBar;
        }

        private MyButton createAccountButton(){
            accountButton = new MyButton("Account");
            initButton(accountButton,accountPanel);
            return accountButton;
        }

        private MyButton createCorsiButton(){
            corsiButton = new MyButton("Corsi");
            clickedButton = corsiButton;
            initButton(corsiButton,accountCorsiPanel);
            return corsiButton;
        }

        private MyButton createImpostazioniButton(){
            impostazioniButton = new MyButton("Impostazioni");
            initButton(impostazioniButton,impostazioniPanel);
            return impostazioniButton;
        }

        private MyButton createCalendarButton(){
            calendarButton = new MyButton("Calendario");
            initButton(calendarButton,calendarioPanel);
            return calendarButton;
        }

        private void showOnlyPanel(Pane panelToShow){
            Pane[] allPanels = {accountPanel, accountCorsiPanel, impostazioniPanel, calendarioPanel};

            for (Pane panel : allPanels) {

                if (panel == panelToShow){
                    panel.setVisible(true);
                    panel.setManaged(true);
                } else {
                    panel.setVisible(false);
                    panel.setManaged(false);
                }
            }

        }


        public CalendarioPanel getCalendarioPanel(){
            return calendarioPanel;
        }
        public void setCalendarioPanel(CalendarioPanel calendarioPanel){
            this.calendarioPanel = calendarioPanel;
        }

        public BorderPane getAccountCorsiPanel(){
            return accountCorsiPanel;
        }
    }
