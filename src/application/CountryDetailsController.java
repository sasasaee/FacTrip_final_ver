package application;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.application.HostServices;

public class CountryDetailsController {
	private HostServices hostServices;
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    @FXML
    private VBox detailsBox;
    
    public void setCountryData(ExploreFactsController.Country country) {
        detailsBox.getChildren().clear();

        HBox mainContainer = new HBox(50); 	//spacing between left and right
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.setPadding(new Insets(20, 50, 20, 50));

        VBox leftContainer = new VBox(15);
        leftContainer.setAlignment(Pos.CENTER);

        if (!country.getFacts().isEmpty()) {
            String fact = country.getFacts().get(0);

            //Splitting the first sentence
            int endOfFirstSentence = fact.indexOf(".”") + 1; //include closing mark
            if (endOfFirstSentence < 0)
            	endOfFirstSentence = fact.length();

            String firstSentence = fact.substring(0, endOfFirstSentence);
            String remainingText = fact.substring(endOfFirstSentence);

            Text italicText = new Text(firstSentence);
            italicText.setStyle("-fx-font-family: 'Arial'; -fx-font-style: italic; -fx-font-size: 20px; -fx-fill: #660000;");

            Text normalText = new Text(remainingText);
            normalText.setStyle("-fx-font-family: 'Cambria Math'; -fx-font-size: 20px; -fx-fill: #660000;");

            TextFlow textFlow = new TextFlow(italicText, normalText);
            textFlow.setPrefWidth(400); //adjust width for wrapping

            leftContainer.getChildren().add(textFlow);
        }

        VBox rightContainer = new VBox(15);
        rightContainer.setAlignment(Pos.TOP_LEFT);
        rightContainer.setPrefWidth(950); //same width for all countries
        //rightContainer.setFillWidth(false);

        if (country.getScenicImagePath() != null) {
            Image scenicImage;
            try {
                scenicImage = new Image(getClass().getResourceAsStream(country.getScenicImagePath()));
            } catch (Exception e) {
                e.printStackTrace();
                scenicImage = new Image(getClass().getResourceAsStream("/application/resources/images/kiribati.png"));
            }
            ImageView imageView = new ImageView(scenicImage);
            imageView.setFitWidth(900);
            
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            rightContainer.getChildren().add(imageView);
        }

        if (country.getFacts().size() > 1) {		//if more than 1 fact
            Label secondFact = new Label(country.getFacts().get(1));
            secondFact.setWrapText(true);
            secondFact.setStyle("-fx-font-family: 'Cambria Math'; -fx-font-size: 20px; -fx-padding: 10px; -fx-text-fill: #660000;");
            rightContainer.getChildren().add(secondFact);
        }

        if (country.getLearnMoreLink() != null && !country.getLearnMoreLink().isEmpty()) {
            Label linkLabel = new Label("\n\n\n\nWanna Know more");
            linkLabel.setStyle("-fx-font-family: 'Cambria Math'; -fx-text-fill: #660000; -fx-underline: true; -fx-font-size: 24px;");
            linkLabel.setOnMouseClicked(e -> {
                if (hostServices != null) {
                    hostServices.showDocument(country.getLearnMoreLink());
                }
            });
            leftContainer.getChildren().add(linkLabel);
        }

        mainContainer.getChildren().addAll(leftContainer, rightContainer);
        detailsBox.getChildren().add(mainContainer);
    }

}
