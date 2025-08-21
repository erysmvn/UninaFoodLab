package GUI.Pane;

import Controller.Controller;
import Entity.*;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.MonthSheetView;
import com.calendarfx.view.page.YearPage;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CalendarioPanel extends Pane {

    private CalendarView calendarView;
    private Calendar sessioniPresenzaCalendar;
    private Calendar sessioniOnlineCalendar;
    private ArrayList<ArrayList<Sessione>> sessioniDeiCorsi;
    private ArrayList<Corso> corsi;
    private Controller controller;

    public CalendarioPanel(Controller controller) {
        this.controller = controller;
        createCalendarView();
        this.disableCalendarFunction();
        this.getChildren().add(calendarView);

    }

    private CalendarView createCalendarView(){
        calendarView = new CalendarView();

        sessioniPresenzaCalendar = new Calendar("Sessioni in presenza");
        sessioniPresenzaCalendar.setStyle(Calendar.Style.STYLE2);
        sessioniPresenzaCalendar.setReadOnly(true);

        sessioniOnlineCalendar = new Calendar("Sessioni online");
        sessioniOnlineCalendar.setStyle(Calendar.Style.STYLE5);
        sessioniOnlineCalendar.setReadOnly(true);

        CalendarSource source = new CalendarSource("Sessioni");
        source.getCalendars().addAll(sessioniPresenzaCalendar, sessioniOnlineCalendar);

        calendarView.getCalendarSources().clear();
        calendarView.getCalendarSources().add(source);

        calendarView.prefWidthProperty().bind(this.widthProperty());
        calendarView.prefHeightProperty().bind(this.heightProperty());

        return calendarView;
    }

    private void disableCalendarFunction(){

        calendarView.setEntryContextMenuCallback(param -> null);
        calendarView.setDateDetailsCallback(param -> null);
        calendarView.setEntryEditPolicy(param->null);
        calendarView.setEntryDetailsPopOverContentCallback(param -> null);
        calendarView.setContextMenuCallback(param -> null);
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);
        calendarView.setShowPageSwitcher(true);

        calendarView.setEntryDetailsCallback(click -> {
            if (click.getEntry() != null && click.getEntry().getUserObject() instanceof Corso corso) {
                if (calendarView.getSelectedPageView() instanceof YearPage) {
                    calendarView.showMonthPage();
                    calendarView.setDate(click.getEntry().getStartDate());
                } else {
                    controller.openCorsoPage(corso);
                }
            }
            return null;
        });

        calendarView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                if (e.getButton() == MouseButton.SECONDARY || e.getClickCount() > 1)
                    e.consume();
        });

        sessioniOnlineCalendar.addEventHandler( e->{
                if ( e.getSource() == MouseEvent.MOUSE_CLICKED)
                    e.consume();

        });
        sessioniPresenzaCalendar.addEventHandler(   e-> {

                if(e.getSource() == MouseEvent.MOUSE_CLICKED)
                    e.consume();
        });
    }



    public void initCalendario(Utente utente) {
        corsi = utente.getCorsi();
        sessioniDeiCorsi = new ArrayList<>();
        for (Corso corso : corsi) {
            sessioniDeiCorsi.add(corso.getSessioni());
        }
        addAllEvents();
        setFirstSessionDay();
    }

    private void addAllEvents() {
        Entry<Object> entry;
        int i = 0;
        for (ArrayList<Sessione> listaSessioni : sessioniDeiCorsi) {
            Corso corso = corsi.get(i++);
            for (Sessione s : listaSessioni) {
                LocalDateTime start = s.getOra();
                LocalDateTime end = start.plusMinutes((long) (s.getDurata() * 60));
                String titolo;
                if (s instanceof SessionePresenza sp) {
                    titolo = corso.getNome() + "\nLezione in presenza\n" + sp.getLuogo() + "\nclick for info";
                    entry = new Entry<>(titolo);
                    entry.setInterval(start, end);
                    entry.setUserObject(corso);
                    sessioniPresenzaCalendar.addEntry(entry);
                } else if (s instanceof SessioneOnline so) {
                    titolo = corso.getNome() + "\nLezione online\n" + so.getLinkIncontro() + "\nclick for info";
                    entry = new Entry<>(titolo);
                    entry.setInterval(start, end);
                    entry.setUserObject(corso);
                    sessioniOnlineCalendar.addEntry(entry);
                }
            }
        }
    }

    private void setFirstSessionDay() {
        LocalDateTime firstDate = null;
        for (ArrayList<Sessione> lista : sessioniDeiCorsi) {
            for (Sessione s : lista) {
                if (firstDate == null || s.getOra().isBefore(firstDate)) {
                    firstDate = s.getOra();
                }
            }
        }
        if (firstDate != null) {
            calendarView.setDate(firstDate.toLocalDate());
            calendarView.showDayPage();
        }
    }
}
