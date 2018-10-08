package pl.edu.agh.multiscalemodelling.engine.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import pl.edu.agh.multiscalemodelling.engine.Application;
import pl.edu.agh.multiscalemodelling.engine.logic.Logic;

public abstract class AbstractScreen implements Screen {

    protected Application application;
    protected Logic logic;
    protected double timer;

    protected Camera cellCamera;
    protected Camera uiCamera;

    protected Viewport cellViewport;
    protected Viewport uiViewport;

    protected Stage stage;

    protected Table root;
    protected Table table;

    protected boolean showProgressbool;
    protected boolean showBordersbool;

    public AbstractScreen(Application application) {

        this.application = application;
        timer = 0;

        cellCamera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();

        cellViewport = new FitViewport(Gdx.graphics.getHeight(), Gdx.graphics.getHeight(), cellCamera);
        uiViewport = new FitViewport(Gdx.graphics.getWidth() - Gdx.graphics.getHeight(), Gdx.graphics.getHeight(), uiCamera);

        cellCamera.position.set(cellViewport.getWorldWidth() / 2, cellViewport.getWorldHeight() / 2, 0);
        uiCamera.position.set(uiViewport.getWorldWidth() / 2, uiViewport.getWorldHeight() / 2, 0);

        stage = new Stage(uiViewport, this.application.getSpriteBatch());

        root = new Table();
        table = new Table();
        table.setBackground(DrawableColor.getColor(Color.DARK_GRAY));
        root.setBackground(DrawableColor.getColor(Color.GRAY));

        root.setFillParent(true);
        root.add(table).width(Gdx.graphics.getWidth() - Gdx.graphics.getHeight()).left().top().expand();

        stage.addActor(root);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        uiViewport.setCamera(uiCamera);
        uiViewport.setScreenBounds(0, 0, Gdx.graphics.getWidth() - Gdx.graphics.getHeight(), Gdx.graphics.getHeight());
        uiViewport.apply();
        application.getSpriteBatch().setProjectionMatrix(uiCamera.combined);
        stage.act();
        stage.draw();

        cellViewport.setCamera(cellCamera);
        cellViewport.setScreenBounds(Gdx.graphics.getWidth() - Gdx.graphics.getHeight(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getHeight());
        cellViewport.apply();
        application.getSpriteBatch().setProjectionMatrix(cellCamera.combined);
        application.getSpriteBatch().begin();
        float width = (float) Gdx.graphics.getHeight() / ((float) logic.getBoard().getGreaterDimesion() / (float) logic.getBoard().getSize().x);
        float height = (float) Gdx.graphics.getHeight() / ((float) logic.getBoard().getGreaterDimesion() / (float) logic.getBoard().getSize().y);
        Texture texture = logic.getBoard().draw(showProgressbool, showBordersbool);
        application.getSpriteBatch().draw(texture, 0, Gdx.graphics.getHeight() - height, width, height);
        application.getSpriteBatch().end();
        texture.dispose();
        Gdx.input.setInputProcessor(stage);

    }

    @Override
    public void resize(int width, int height) {

        cellViewport.setWorldSize(height, height);
        cellViewport.update(width, height, true);
        uiViewport.setWorldSize(width - height, height);
        uiViewport.update(width, height, true);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        application.dispose();
        stage.dispose();

    }

    public void update(float delta) {


        cellCamera.update();
        uiCamera.update();


    }

}
