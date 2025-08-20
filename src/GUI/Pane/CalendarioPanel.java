package GUI.Pane;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import javafx.scene.layout.Pane;
import java.time.LocalDateTime;

public class CalendarioPanel extends Pane {

    private final CalendarView calendarView;
    private final Calendar myCalendar;

    public CalendarioPanel() {

        calendarView = new CalendarView();
        myCalendar = new Calendar("Le tue Sessioni");
        myCalendar.setStyle(Calendar.Style.STYLE7);

        CalendarSource myCalendarSource = new CalendarSource("Il mio Calendario");
        myCalendarSource.getCalendars().add(myCalendar);

        calendarView.getCalendarSources().clear();
        calendarView.getCalendarSources().add(myCalendarSource);

        calendarView.setShowPageSwitcher(true);
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);

        this.getChildren().add(calendarView);

        calendarView.prefWidthProperty().bind(this.widthProperty());
        calendarView.prefHeightProperty().bind(this.heightProperty());
    }

    public void addEntry(String title, LocalDateTime start, LocalDateTime end) {
        Entry<String> entry = new Entry<>(title);
        entry.setInterval(start, end);
        myCalendar.addEntry(entry);
    }
}
