package pl.edu.agh.multiscalemodelling.processsimulation

import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import com.badlogic.gdx.graphics.g2d.{BitmapFont, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.utils.{ChangeListener, ClickListener, TextureRegionDrawable}
import pl.edu.agh.multiscalemodelling.engine.Application
import pl.edu.agh.multiscalemodelling.engine.logic.neighbourhood.MooreNeighbourHood
import pl.edu.agh.multiscalemodelling.engine.render.{AbstractScreen, DrawableColor}
import pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth.{NaiveSeedsGrowthBoard, NaiveSeedsGrowthLogic}

class SimulationScreen(application: Application) extends AbstractScreen(application) {

  var widthField: TextField = _
  var heightField: TextField = _
  var seedField: TextField = _
  var timeField: TextField = _
  var inclusionAmountField: TextField = _
  var inclusionSizeField: TextField = _
  var widthLabel: Label = _
  var heightLabel: Label = _
  var seedLabel: Label = _
  var inclusionAmountLabel: Label = _
  var inclusionSizeLabel: Label = _
  var continousSeeding: CheckBox = _
  var showProgress: CheckBox = _
  var showBorders: CheckBox = _
  var seedButton: Button = _
  var toggleButton: Button = _
  var clearButton: Button = _
  var nextButton: Button = _
  var resizeButton: Button = _
  var importButton: Button = _
  var exportButton: Button = _
  var addInclusionsButton: Button = _
  var neighbourhoodSelection: SelectBox[String] = _
  var boundaryConditionSelection: SelectBox[String] = _
  var seedTypeSelection: SelectBox[String] = _
  var fileTypeSelection: SelectBox[String] = _
  var inclusionType: SelectBox[String] = _
  var naiveSeedsGrowthLogic: NaiveSeedsGrowthLogic = _
  widthLabel = new Label("width", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  heightLabel = new Label("height", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  seedLabel = new Label("", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  seedLabel.setVisible(false)
  inclusionAmountLabel = new Label("Inclusions amount", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  inclusionSizeLabel = new Label("Inclusion size", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  val textFieldStyle = new TextField.TextFieldStyle
  textFieldStyle.font = new BitmapFont
  textFieldStyle.fontColor = Color.BLACK
  textFieldStyle.background = DrawableColor.getColor(Color.WHITE)
  widthField = new TextField("700", textFieldStyle)
  heightField = new TextField("700", textFieldStyle)
  seedField = new TextField("5", textFieldStyle)
  timeField = new TextField("1", textFieldStyle)
  inclusionAmountField = new TextField("1", textFieldStyle)
  inclusionSizeField = new TextField("2", textFieldStyle)
  seedField.setVisible(false)
  val textButtonStyle = new TextButton.TextButtonStyle
  textButtonStyle.font = new BitmapFont
  textButtonStyle.fontColor = Color.WHITE
  textButtonStyle.overFontColor = Color.BLACK
  textButtonStyle.over = DrawableColor.getColor(Color.WHITE)
  var pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888)
  pixmap.setColor(Color.WHITE)
  pixmap.fill()
  val drawable1 = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)))
  pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888)
  pixmap.setColor(Color.BLACK)
  pixmap.fill()
  val drawable2 = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)))
  val checkBoxStyle = new CheckBox.CheckBoxStyle(drawable1, drawable2, new BitmapFont, Color.WHITE)
  continousSeeding = new CheckBox("Continous nucleons", checkBoxStyle)
  showProgress = new CheckBox("Show progress", checkBoxStyle)
  showBorders = new CheckBox("Show borders", checkBoxStyle)
  seedButton = new TextButton("Place nucleons", textButtonStyle)
  toggleButton = new TextButton("Play", textButtonStyle)
  clearButton = new TextButton("Clear", textButtonStyle)
  nextButton = new TextButton("Next", textButtonStyle)
  resizeButton = new TextButton("Resize", textButtonStyle)
  importButton = new TextButton("Import", textButtonStyle)
  exportButton = new TextButton("Export", textButtonStyle)
  addInclusionsButton = new TextButton("Add inclusions", textButtonStyle)
  seedButton.addListener(new ClickListener)
  toggleButton.addListener(new ClickListener)
  clearButton.addListener(new ClickListener)
  nextButton.addListener(new ClickListener)
  resizeButton.addListener(new ClickListener)
  importButton.addListener(new ClickListener)
  exportButton.addListener(new ClickListener)
  addInclusionsButton.addListener(new ClickListener)
  val selectBoxStyle = new SelectBox.SelectBoxStyle
  selectBoxStyle.font = new BitmapFont
  selectBoxStyle.fontColor = Color.BLACK
  selectBoxStyle.background = DrawableColor.getColor(Color.WHITE)
  val listStyle = new List.ListStyle
  listStyle.background = DrawableColor.getColor(Color.WHITE)
  listStyle.font = new BitmapFont
  listStyle.fontColorSelected = Color.WHITE
  listStyle.fontColorUnselected = Color.BLACK
  listStyle.selection = DrawableColor.getColor(Color.BLACK)
  selectBoxStyle.listStyle = listStyle
  val scrollPaneStyle = new ScrollPane.ScrollPaneStyle
  scrollPaneStyle.background = DrawableColor.getColor(Color.WHITE)
  scrollPaneStyle.hScrollKnob = DrawableColor.getColor(Color.WHITE)
  scrollPaneStyle.hScroll = DrawableColor.getColor(Color.BLUE)
  scrollPaneStyle.vScroll = DrawableColor.getColor(Color.BLUE)
  scrollPaneStyle.vScrollKnob = DrawableColor.getColor(Color.WHITE)
  selectBoxStyle.scrollStyle = new ScrollPane.ScrollPaneStyle(scrollPaneStyle)
  neighbourhoodSelection = new SelectBox[String](selectBoxStyle)
  neighbourhoodSelection.setItems("Moore")
  neighbourhoodSelection.setSelectedIndex(0)
  neighbourhoodSelection.addListener(new ChangeListener() {
    override def changed(event: ChangeListener.ChangeEvent, actor: Actor): Unit = logic.getBoard.setNeighbourhood(logic.getBoard.neighborhoods.get(neighbourhoodSelection.getSelectedIndex), logic.getBoard.boundaryConditions.get(boundaryConditionSelection.getSelectedIndex))
  })
  boundaryConditionSelection = new SelectBox[String](selectBoxStyle)
  boundaryConditionSelection.setItems(//"Fixed",
    "Periodic")
  boundaryConditionSelection.setSelectedIndex(0)
  boundaryConditionSelection.addListener(new ChangeListener() {
    override def changed(event: ChangeListener.ChangeEvent, actor: Actor): Unit = logic.getBoard.setNeighbourhood(logic.getBoard.neighborhoods.get(neighbourhoodSelection.getSelectedIndex), logic.getBoard.boundaryConditions.get(boundaryConditionSelection.getSelectedIndex))
  })
  seedTypeSelection = new SelectBox[String](selectBoxStyle)
  seedTypeSelection.setItems("Random seed", "Regular Seed", "Random with radius")
  seedTypeSelection.setSelectedIndex(0)
  seedTypeSelection.addListener(new ChangeListener() {
    override def changed(event: ChangeListener.ChangeEvent, actor: Actor): Unit = seedTypeSelection.getSelectedIndex match {
      case 0 =>
        seedField.setVisible(false)
        seedLabel.setVisible(false)
      case 1 =>
        seedField.setVisible(true)
        seedLabel.setVisible(true)
        seedLabel.setText("Distance")
      case 2 =>
        seedField.setVisible(true)
        seedLabel.setVisible(true)
        seedLabel.setText("Radius")
    }
  })

  fileTypeSelection = new SelectBox[String](selectBoxStyle)
  fileTypeSelection.setItems("Json", "Bitmap")
  fileTypeSelection.setSelectedIndex(0)
  fileTypeSelection.addListener(new ChangeListener {
    override def changed(event: ChangeListener.ChangeEvent, actor: Actor): Unit = seedTypeSelection.getSelectedIndex match {
      case 0 =>
    }
  })

  inclusionType = new SelectBox[String](selectBoxStyle)
  inclusionType.setItems("Circle", "Square")
  inclusionType.setSelectedIndex(0)
  inclusionType.addListener(new ChangeListener {
    override def changed(event: ChangeListener.ChangeEvent, actor: Actor): Unit = seedTypeSelection.getSelectedIndex match {
      case 0 =>
      case 1 =>
    }
  })

  table.add(widthLabel).expandX
  table.add(heightLabel).expandX
  table.row
  table.add(widthField).width(uiViewport.getWorldWidth / 2)
  table.add(heightField).width(uiViewport.getWorldWidth / 2)
  table.row
  table.add(seedButton).expandX.fill
  table.add(seedTypeSelection).expandX.fill
  table.row
  table.add(clearButton).expandX.fill
  table.add(nextButton).expandX.fill
  table.row
  table.add(resizeButton).expandX.fill
  table.add(toggleButton).expandX.fill
  table.row
  table.add(neighbourhoodSelection).expandX.fill
  table.add(boundaryConditionSelection).expandX.fill
  table.row
  table.add(seedLabel).expandX.fill
  table.add(seedField).expandX.fill
  table.row
  table.add(continousSeeding).expandX.fill
  table.add(timeField).expandX.fill
  table.row
  table.add(showProgress).expandX.fill
  table.add(importButton).expandX.fill
  table.row
  table.add(showBorders).expandX.fill
  table.add(exportButton).expandX.fill
  table.row
  table.add(fileTypeSelection).expandX.fill
  table.add(inclusionType).expandX.fill
  table.row
  table.add(inclusionAmountLabel).expandX.fill
  table.add(inclusionAmountField).expandX.fill
  table.row
  table.add(inclusionSizeLabel).expandX.fill
  table.add(inclusionSizeField).expandX.fill
  table.row
  table.add(addInclusionsButton).expandX.fill

  naiveSeedsGrowthLogic = new NaiveSeedsGrowthLogic(300, 300)
  logic = naiveSeedsGrowthLogic
  logic.getBoard.setNeighbourhood(MooreNeighbourHood, logic.getBoard.boundaryConditions.get(boundaryConditionSelection.getSelectedIndex))

  override def update(delta: Float): Unit = {
    super.update(delta)
    timer += delta
  }

  override def render(delta: Float): Unit = {
    super.render(delta)
    showProgressbool = showProgress.isChecked
    showBordersbool = showBorders.isChecked
    update(delta)
    if (!logic.isPaused) {
      logic.started=true
      logic.iterate()
      if (timer > timeField.getText.toFloat && continousSeeding.isChecked) {
        timer = 0
        logic.getBoard.seed()
      }
    }
    handleInput()
  }

  override def resize(width: Int, height: Int): Unit = super.resize(width, height)

  override def dispose(): Unit = super.dispose()

  def handleInput(): Unit = {
    if (Gdx.input.justTouched) {
      var vector2 = new Vector2(Gdx.input.getX.toFloat, Gdx.graphics.getHeight.toFloat - Gdx.input.getY)
      vector2 = cellViewport.unproject(vector2)
      if (vector2.x > 0 && vector2.y > 0) logic.click((vector2.x / (Gdx.graphics.getHeight / logic.getBoard.size.x)).toInt, (vector2.y / (Gdx.graphics.getHeight / logic.getBoard.size.y)).toInt)
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.P)) logic.pause()
    if (seedButton.isPressed && Gdx.input.justTouched) seedTypeSelection.getSelectedIndex match {
      case 0 =>
        logic.getBoard.seed()
      case 1 =>
        logic.getBoard.asInstanceOf[NaiveSeedsGrowthBoard].seed(seedField.getText.toInt)
      case 2 =>
        logic.getBoard.asInstanceOf[NaiveSeedsGrowthBoard].radiusSeed(seedField.getText.toInt)
    }
    if (toggleButton.isPressed && Gdx.input.justTouched) logic.pause()
    if (clearButton.isPressed && Gdx.input.justTouched) logic.getBoard.clear()
    if (nextButton.isPressed && Gdx.input.justTouched && logic.isPaused) logic.iterate()
    if (resizeButton.isPressed && Gdx.input.justTouched) {
      logic = new NaiveSeedsGrowthLogic(widthField.getText.toInt, heightField.getText.toInt)
       logic.getBoard.setNeighbourhood(MooreNeighbourHood, logic.getBoard.boundaryConditions.get(boundaryConditionSelection.getSelectedIndex))
    }

    if(importButton.isPressed && Gdx.input.justTouched) logic.getBoard.load()
    if(exportButton.isPressed && Gdx.input.justTouched) logic.getBoard.save()
    if(addInclusionsButton.isPressed && Gdx.input.justTouched) logic.getBoard.addInclusions(inclusionType.getSelectedIndex, inclusionAmountField.getText.toInt, inclusionSizeField.getText.toInt, logic.started)

  }

}