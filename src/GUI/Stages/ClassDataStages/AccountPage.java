    package GUI.Stages.ClassDataStages;

    import Controller.Controller;
    import Entity.*;
    import GUI.Pane.*;
    import GUI.Buttons.*;
    import GUI.Stages.MyStage;
    import javafx.geometry.Insets;
    import javafx.scene.layout.*;
    import javafx.scene.paint.Color;

    public class AccountPage extends MyStage {
        Controller controller;
        BorderPane root;
        StackPane content;

        MyButton impostazioniButton;
        MyButton accountButton;
        MyButton corsiButton;
        MyButton calendarButton;
        MyButton clickedButton;
        MyButton reportButton;
        ReportPanel reportPanel;

        VBox impostazioniPanel;
        VBox accountPanel;
        ElencoCorsiPanel elencoCorsiPanel;
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
            elencoCorsiPanel = createAccountCorsiPanel();
            impostazioniPanel = new ImpostazioniPanel(controller);
            calendarioPanel = new CalendarioPanel(controller);
            calendarioPanel.initCalendario(utente);

            content.getChildren().addAll(elencoCorsiPanel,calendarioPanel,accountPanel,impostazioniPanel);
            reportPanel = new ReportPanel(controller);
            if(utente instanceof Chef){
                reportPanel.initPanel(utente);
                reportPanel.initialize();
                content.getChildren().add(reportPanel);
            }

            HBox topBar = createTopBar();
            root.setTop(topBar);
            root.setCenter(content);


            showOnlyPanel(elencoCorsiPanel);
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

            if(reportButton!=null){
                if(clickedButton != reportButton) reportButton.setOutlineNotClickedStyle();
                else reportButton.setOutlineClickedStyle();
            }

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

            if(utente instanceof Chef){
                reportButton = createReportButton();
                topBar.getChildren().addAll(corsiButton, calendarButton, accountButton, impostazioniButton, reportButton, spacer, minimizeBtn, closeBtn);
            }else{
                topBar.getChildren().addAll(corsiButton, calendarButton, accountButton, impostazioniButton, spacer, minimizeBtn, closeBtn);
            }

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
            initButton(corsiButton, elencoCorsiPanel);
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

        private MyButton createReportButton(){
            reportButton = new MyButton("Report");
            initButton(reportButton, reportPanel);
            return reportButton;
        }

        private void showOnlyPanel(Pane panelToShow){
            Pane[] allPanels = {accountPanel, elencoCorsiPanel, impostazioniPanel, calendarioPanel, reportPanel};

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
    }
