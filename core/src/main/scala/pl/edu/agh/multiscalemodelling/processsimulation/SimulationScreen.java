package pl.edu.agh.multiscalemodelling.processsimulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import pl.edu.agh.multiscalemodelling.engine.Application;
import pl.edu.agh.multiscalemodelling.engine.render.AbstractScreen;
import pl.edu.agh.multiscalemodelling.engine.render.DrawableColor;
//import pl.edu.agh.multiscalemodelling.processsimulation.montecarlo.MonteCarloLogic;
import pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth.NaiveSeedsGrowthBoard;
import pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth.NaiveSeedsGrowthLogic;

public class SimulationScreen extends AbstractScreen {

    private TextField widthField;
    private TextField heightField;
    private TextField seedField;
    private TextField timeField;
    private Label widthLabel;
    private Label heightLabel;
    private Label seedLabel;
    private CheckBox continousSeeding;
    private CheckBox showProgress;
    private CheckBox showBorders;
    private Button seedButton;
    private Button toggleButton;
    private Button clearButton;
    private Button nextButton;
    private Button resizeButton;
    //private Button monteCarloButton;
    private SelectBox<String> neighbourhoodSelection;
    private SelectBox<String> boundaryConditionSelection;
    private SelectBox<String> seedTypeSelection;

    private NaiveSeedsGrowthLogic naiveSeedsGrowthLogic;
    //private MonteCarloLogic monteCarloLogic;

    public SimulationScreen(Application application) {

        super(application);
        this.application = application;

        widthLabel = new Label("width", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        heightLabel = new Label("height", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        seedLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        seedLabel.setVisible(false);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = new BitmapFont();
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.background = DrawableColor.getColor(Color.WHITE);

        widthField = new TextField("700", textFieldStyle);
        heightField = new TextField("700", textFieldStyle);
        seedField = new TextField("5", textFieldStyle);
        timeField = new TextField("1", textFieldStyle);
        seedField.setVisible(false);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.overFontColor = Color.BLACK;
        textButtonStyle.over = DrawableColor.getColor(Color.WHITE);

        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        Drawable drawable1 = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        Drawable drawable2 = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle(drawable1, drawable2, new BitmapFont(), Color.WHITE);

        continousSeeding = new CheckBox("Continous seeding", checkBoxStyle);
        showProgress = new CheckBox("Show progress", checkBoxStyle);
        showBorders = new CheckBox("Show borders", checkBoxStyle);

        seedButton = new TextButton("Seed", textButtonStyle);
        toggleButton = new TextButton("Play", textButtonStyle);
        clearButton = new TextButton("Clear", textButtonStyle);
        nextButton = new TextButton("Next", textButtonStyle);
        resizeButton = new TextButton("Resize", textButtonStyle);
        //monteCarloButton = new TextButton("Monte Carlo", textButtonStyle);

        seedButton.addListener(new ClickListener());
        toggleButton.addListener(new ClickListener());
        clearButton.addListener(new ClickListener());
        nextButton.addListener(new ClickListener());
        resizeButton.addListener(new ClickListener());
        //monteCarloButton.addListener(new ClickListener());

        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = new BitmapFont();
        selectBoxStyle.fontColor = Color.BLACK;
        selectBoxStyle.background = DrawableColor.getColor(Color.WHITE);
        List.ListStyle listStyle = new List.ListStyle();
        listStyle.background = DrawableColor.getColor(Color.WHITE);
        listStyle.font = new BitmapFont();
        listStyle.fontColorSelected = Color.WHITE;
        listStyle.fontColorUnselected = Color.BLACK;
        listStyle.selection = DrawableColor.getColor(Color.BLACK);
        selectBoxStyle.listStyle = listStyle;
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = DrawableColor.getColor(Color.WHITE);
        scrollPaneStyle.hScrollKnob = DrawableColor.getColor(Color.WHITE);
        scrollPaneStyle.hScroll = DrawableColor.getColor(Color.BLUE);
        scrollPaneStyle.vScroll = DrawableColor.getColor(Color.BLUE);
        scrollPaneStyle.vScrollKnob = DrawableColor.getColor(Color.WHITE);
        selectBoxStyle.scrollStyle = new ScrollPane.ScrollPaneStyle(scrollPaneStyle);

        neighbourhoodSelection = new SelectBox<>(selectBoxStyle);

        neighbourhoodSelection.setItems(
                "Moore"
/*                "Newman",
                "Pentagonal random",
                "Hexagonal random",
                "Hexagonal left",
                "Hexagonal right"*/
        );

        neighbourhoodSelection.setSelectedIndex(0);

        neighbourhoodSelection.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logic.getBoard().setNeighbourhood(logic.getBoard().getNeighborhoods().get(neighbourhoodSelection.getSelectedIndex()), logic.getBoard().getBoundaryConditions().get(boundaryConditionSelection.getSelectedIndex()));
            }

        });

        boundaryConditionSelection = new SelectBox<>(selectBoxStyle);

        boundaryConditionSelection.setItems(
                //"Fixed",
                "Periodic"
        );

        boundaryConditionSelection.setSelectedIndex(0);

        boundaryConditionSelection.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logic.getBoard().setNeighbourhood(logic.getBoard().getNeighborhoods().get(neighbourhoodSelection.getSelectedIndex()), logic.getBoard().getBoundaryConditions().get(boundaryConditionSelection.getSelectedIndex()));
            }

        });

        seedTypeSelection = new SelectBox<>(selectBoxStyle);

        seedTypeSelection.setItems(
                "Random seed",
                "Regular Seed",
                "Random with radius"
        );

        seedTypeSelection.setSelectedIndex(0);

        seedTypeSelection.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                switch (seedTypeSelection.getSelectedIndex()) {

                    case 0:
                        seedField.setVisible(false);
                        seedLabel.setVisible(false);
                        break;
                    case 1:
                        seedField.setVisible(true);
                        seedLabel.setVisible(true);
                        seedLabel.setText("Distance");
                        break;
                    case 2:
                        seedField.setVisible(true);
                        seedLabel.setVisible(true);
                        seedLabel.setText("Radius");
                        break;

                }
            }
        });

        table.add(widthLabel).expandX();
        table.add(heightLabel).expandX();
        table.row();
        table.add(widthField).width(uiViewport.getWorldWidth() / 2);
        table.add(heightField).width(uiViewport.getWorldWidth() / 2);
        table.row();
        table.add(seedButton).expandX().fill();
        table.add(seedTypeSelection).expandX().fill();
        table.row();
        table.add(clearButton).expandX().fill();
        table.add(nextButton).expandX().fill();
        table.row();
        table.add(resizeButton).expandX().fill();
        table.add(toggleButton).expandX().fill();
        table.row();
        table.add(neighbourhoodSelection).expandX().fill();
        table.add(boundaryConditionSelection).expandX().fill();
        table.row();
        table.add(seedLabel).expandX().fill();
        table.add(seedField).expandX().fill();
        table.row();
        table.add(continousSeeding).expandX().fill();
        table.add(timeField).expandX().fill();
        table.row();
        //table.add(recrystallizeButton).expandX().fill();
        table.add(showProgress).expandX().fill();
        table.row();
        //table.add(monteCarloButton).expandX().fill();
        table.add(showBorders).expandX().fill();

        naiveSeedsGrowthLogic = new NaiveSeedsGrowthLogic(100, 100);
        logic = naiveSeedsGrowthLogic;


        logic.getBoard().setNeighbourhood(logic.getBoard().getNeighborhoods().get(neighbourhoodSelection.getSelectedIndex()), logic.getBoard().getBoundaryConditions().get(boundaryConditionSelection.getSelectedIndex()));

    }

    public void update(float delta) {

        super.update(delta);
        timer += delta;

    }


    @Override
    public void render(float delta) {

        super.render(delta);

        showProgressbool = showProgress.isChecked();
        showBordersbool = showBorders.isChecked();

        update(delta);

        if (!logic.isPaused()) {
            logic.iterate();
            if (timer > Float.parseFloat(timeField.getText()) && continousSeeding.isChecked()) {
                timer = 0;
                logic.getBoard().seed();
            }
        }

        handleInput();

    }

    @Override
    public void resize(int width, int height) {

        super.resize(width, height);

    }

    @Override
    public void dispose() {

        super.dispose();

    }

    public void handleInput() {


        if (Gdx.input.justTouched()) {

            Vector2 vector2 = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            vector2 = cellViewport.unproject(vector2);

            if (vector2.x > 0 && vector2.y > 0) {
                logic.click((int) (vector2.x / (Gdx.graphics.getHeight() / logic.getBoard().getSize().x)), (int) (vector2.y / (Gdx.graphics.getHeight() / logic.getBoard().getSize().y)));
            }

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {

            logic.pause();

        }

        if (seedButton.isPressed() && Gdx.input.justTouched()) {

            switch (seedTypeSelection.getSelectedIndex()) {
                case 0:
                    logic.getBoard().seed();
                    break;
                case 1:
                    ((NaiveSeedsGrowthBoard) logic.getBoard()).seed(Integer.parseInt(seedField.getText()));
                    break;
                case 2:
                    ((NaiveSeedsGrowthBoard) logic.getBoard()).radiusSeed(Integer.parseInt(seedField.getText()));
                    break;
            }

        }

        if (toggleButton.isPressed() && Gdx.input.justTouched()) {

            logic.pause();

        }

        if (clearButton.isPressed() && Gdx.input.justTouched()) {

            logic.getBoard().clear();

        }

        if (nextButton.isPressed() && Gdx.input.justTouched() && logic.isPaused()) {

            logic.iterate();

        }

        if (resizeButton.isPressed() && Gdx.input.justTouched()) {

            logic = new NaiveSeedsGrowthLogic(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
            logic.getBoard().setNeighbourhood(logic.getBoard().getNeighborhoods().get(neighbourhoodSelection.getSelectedIndex()), logic.getBoard().getBoundaryConditions().get(boundaryConditionSelection.getSelectedIndex()));

        }

/*        if (monteCarloButton.isPressed() && Gdx.input.justTouched()) {

            monteCarloLogic = new MonteCarloLogic(logic);

            monteCarloLogic.getBoard().setNeighbourhood(logic.getBoard().getNeighborhoods().get(neighbourhoodSelection.getSelectedIndex()), logic.getBoard().getBoundaryConditions().get(boundaryConditionSelection.getSelectedIndex()));

            logic = monteCarloLogic;

        }*/

    }

}
