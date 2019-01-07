package fr.pa1007.song.controller;

import club.minnced.discord.rpc.DiscordRichPresence;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MainAppController {

    public ToggleButton toggleButton;
    public TextField    durBox;
    public ImageView    coverImage;
    public TextField    titleBox;
    public TextField    authorBox;
    public TextField    deezLink;

    /**
     * The boolean to check if the button is toggle or not
     */
    private boolean toggled;

    /**
     * This change the display of the button and change the toggled boolean
     *
     * @param actionEvent the action perform
     */
    public void toggleButtonHandler(ActionEvent actionEvent) {
        toggleButton.setText(toggleButton.getText().equals(">") ? "<" : ">");
        toggled = !toggled;
    }


    public void goTOHandler(ActionEvent actionEvent) throws URISyntaxException, IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(deezLink.getText()));
        }
    }

    /**
     * This put an image on the app, can be a not found if the data has not been found
     *
     * @param imageString the link to the image
     */
    public void setImage(String imageString) {
        coverImage.setImage(new Image(imageString));
    }

    /**
     * Set the link in the box
     *
     * @param link the link to the song
     */
    public void setLinkBox(String link) {
        deezLink.setText(link);
    }

    /**
     * Set the author 's name in the box
     *
     * @param author the author's name of the song
     */
    public void setAuthorBox(String author) {
        authorBox.setText(author);
    }

    /**
     * Set the title in the box
     *
     * @param title the title of the song
     */
    public void setTitleBox(String title) {
        titleBox.setText(title);
    }

    /**
     * This get the duration and set to the discord endTimeStamp for creating a count to the end of the song
     *
     * @param duration the duration of the song, -1 means not found
     * @param presence the discord presence to update
     */
    public void setDuration(Integer duration, DiscordRichPresence presence) {
        durBox.setText(String.valueOf(duration));
        if (toggled && duration != -1) {
            presence.endTimestamp = System.currentTimeMillis() / 1000 + duration - 2;
        }
        else {
            presence.endTimestamp = 0;
        }
    }

    /**
     * Generated from the FXML file
     * This copy the title box
     *
     * @param actionEvent the action perform
     */
    public void cTitleHandler(ActionEvent actionEvent) {
        copy(titleBox);
    }

    /**
     * Generated from the FXML file
     * This copy the Author box
     *
     * @param actionEvent the action perform
     */
    public void cAuthorhandler(ActionEvent actionEvent) {
        copy(authorBox);
    }

    /**
     * Generated from the FXML file
     * This copy the Duration box
     *
     * @param actionEvent the action perform
     */
    public void cDurHandler(ActionEvent actionEvent) {
        copy(durBox);
    }

    /**
     * This easyli copy a text in a field  and go in your clipboard
     *
     * @param field the field use and from
     */
    private void copy(TextField field) {
        final Clipboard clipboard       = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(field.getText());
        clipboard.setContents(stringSelection, null);
    }
}
