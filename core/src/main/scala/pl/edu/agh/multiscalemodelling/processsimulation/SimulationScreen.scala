package pl.edu.agh.multiscalemodelling.processsimulation

import com.badlogic.gdx.{Gdx, Input}
import com.badlogic.gdx.graphics.{Color, Pixmap, Texture}
import com.badlogic.gdx.graphics.g2d.{BitmapFont, TextureRegion}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.utils.{ChangeListener, ClickListener, TextureRegionDrawable}
import pl.edu.agh.multiscalemodelling.engine.Application
import pl.edu.agh.multiscalemodelling.engine.logic.enumeration.{EnergyDistributionType, OperationMode, RenderMode}
import pl.edu.agh.multiscalemodelling.engine.logic.Logic
import pl.edu.agh.multiscalemodelling.engine.logic.neighbourhood.MooreNeighbourHood
import pl.edu.agh.multiscalemodelling.engine.render.{AbstractScreen, DrawableColor}
import pl.edu.agh.multiscalemodelling.processsimulation.naiveseedsgrowth.{NaiveSeedsGrowthBoard, NaiveSeedsGrowthCell, NaiveSeedsGrowthLogic}

class SimulationScreen(application: Application) extends AbstractScreen(application) {

  var widthField: TextField = _
  var heightField: TextField = _
  var seedField: TextField = _
  var inclusionAmountField: TextField = _
  var inclusionSizeField: TextField = _
  var rule4ProbabilityField: TextField = _
  var borderThicknessField: TextField = _
  var statesAmount: TextField = _
  var mcsSteps: TextField = _
  var energyTextField: TextField = _
  var boundaryEnergyTextField: TextField = _
  var nucleonsAmount: TextField = _
  var nucleonsIncrement: TextField = _

  var widthLabel: Label = _
  var heightLabel: Label = _
  var seedLabel: Label = _
  var inclusionAmountLabel: Label = _
  var inclusionSizeLabel: Label = _
  var borderThicknessLabel: Label = _
  var statesAmountLabel: Label = _
  var mcsStepsAmount: Label = _
  var energyDistributionTypeField: Label = _
  var nucleationModeLabel: Label = _
  var nucleonsAmountLabel: Label = _
  var incrementRate: Label = _
  var nucleonsPlacementLabel: Label = _

  var grainShapeControl: CheckBox = _
  var renderEnergy: CheckBox = _

  var seedButton: Button = _
  var toggleButton: TextButton = _
  var clearButton: Button = _
  var nextButton: Button = _
  var resizeButton: Button = _
  var importButton: Button = _
  var exportButton: Button = _
  var addInclusionsButton: Button = _
  var applyButton: Button = _
  var showBorders: Button = _
  var applyMcs: Button = _
  var recrystallizeButton: Button = _

  var neighbourhoodSelection: SelectBox[String] = _
  var fileTypeSelection: SelectBox[String] = _
  var inclusionType: SelectBox[String] = _
  var secondStepSelection: SelectBox[String] = _
  var energyDistributionTypeSelection: SelectBox[String] = _
  var nucleationMode: SelectBox[String] = _
  var nucleonsPlacement: SelectBox[String] = _

  var stepsAmount: Int = 0

  var naiveSeedsGrowthLogic: NaiveSeedsGrowthLogic = _

  widthLabel = new Label("width", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  heightLabel = new Label("height", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  inclusionAmountLabel = new Label("Inclusions amount", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  inclusionSizeLabel = new Label("Inclusion size", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  borderThicknessLabel = new Label("Border thickness", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  statesAmountLabel = new Label("States amount", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  mcsStepsAmount = new Label("Mcs steps amount", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  energyDistributionTypeField = new Label("Energy distribution", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  nucleationModeLabel = new Label("Nucleation mode", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  nucleonsAmountLabel = new Label("Nucleons amount", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  incrementRate = new Label("increment rate", new Label.LabelStyle(new BitmapFont, Color.WHITE))
  nucleonsPlacementLabel = new Label("Placement", new Label.LabelStyle(new BitmapFont, Color.WHITE))

  val textFieldStyle = new TextField.TextFieldStyle
  textFieldStyle.font = new BitmapFont
  textFieldStyle.fontColor = Color.BLACK
  textFieldStyle.background = DrawableColor.getColor(Color.WHITE)
  widthField = new TextField("300", textFieldStyle)
  heightField = new TextField("300", textFieldStyle)
  seedField = new TextField("100", textFieldStyle)
  inclusionAmountField = new TextField("1", textFieldStyle)
  inclusionSizeField = new TextField("2", textFieldStyle)
  rule4ProbabilityField = new TextField("10", textFieldStyle)
  borderThicknessField = new TextField("1", textFieldStyle)
  statesAmount = new TextField("3", textFieldStyle)
  mcsSteps = new TextField("100", textFieldStyle)
  energyTextField = new TextField("4", textFieldStyle)
  boundaryEnergyTextField = new TextField("7", textFieldStyle)
  nucleonsAmount = new TextField("10", textFieldStyle)
  nucleonsIncrement = new TextField("10", textFieldStyle)

  boundaryEnergyTextField.setVisible(false)
  nucleonsIncrement.setVisible(false)
  incrementRate.setVisible(false)

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

  grainShapeControl = new CheckBox("Grain shape control", checkBoxStyle)
  renderEnergy = new CheckBox("Render Energy", checkBoxStyle)

  seedButton = new TextButton("Place nucleons", textButtonStyle)
  toggleButton = new TextButton("Play", textButtonStyle)
  clearButton = new TextButton("Clear", textButtonStyle)
  nextButton = new TextButton("Next", textButtonStyle)
  resizeButton = new TextButton("Resize", textButtonStyle)
  importButton = new TextButton("Import", textButtonStyle)
  exportButton = new TextButton("Export", textButtonStyle)
  addInclusionsButton = new TextButton("Add inclusions", textButtonStyle)
  applyButton = new TextButton("Apply", textButtonStyle)
  showBorders = new TextButton("Show borders", textButtonStyle)
  applyMcs = new TextButton("Apply MCS", textButtonStyle)
  recrystallizeButton = new TextButton("Recrystallize", textButtonStyle)

  seedButton.addListener(new ClickListener)
  toggleButton.addListener(new ClickListener)
  clearButton.addListener(new ClickListener)
  nextButton.addListener(new ClickListener)
  resizeButton.addListener(new ClickListener)
  importButton.addListener(new ClickListener)
  exportButton.addListener(new ClickListener)
  addInclusionsButton.addListener(new ClickListener)
  applyButton.addListener(new ClickListener)
  showBorders.addListener(new ClickListener)
  applyMcs.addListener(new ClickListener)
  recrystallizeButton.addListener(new ClickListener)

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
    override def changed(event: ChangeListener.ChangeEvent, actor: Actor): Unit = logic.getBoard.setNeighbourhood(logic.getBoard.neighborhoods.get(neighbourhoodSelection.getSelectedIndex), logic.getBoard.boundaryConditions.get(0))
  })

  fileTypeSelection = new SelectBox[String](selectBoxStyle)
  fileTypeSelection.setItems("Json", "Bitmap")
  fileTypeSelection.setSelectedIndex(0)

  inclusionType = new SelectBox[String](selectBoxStyle)
  inclusionType.setItems("Circle", "Square")
  inclusionType.setSelectedIndex(0)

  secondStepSelection = new SelectBox[String](selectBoxStyle)
  secondStepSelection.setItems("Substructure", "Dualphase")
  secondStepSelection.setSelectedIndex(0)

  grainBoundaryRenderType = new SelectBox[String](selectBoxStyle)
  grainBoundaryRenderType.setItems("All","Selected")
  grainBoundaryRenderType.setSelectedIndex(0)

  energyDistributionTypeSelection = new SelectBox[String](selectBoxStyle)
  energyDistributionTypeSelection.setItems("Homogenous", "Heterogenous")
  energyDistributionTypeSelection.setSelectedIndex(0)
  energyDistributionTypeSelection.addListener(new ChangeListener {
    override def changed(event: ChangeListener.ChangeEvent, actor: Actor): Unit = energyDistributionTypeSelection.getSelectedIndex match {

      case 0 => boundaryEnergyTextField.setVisible(false)
      case 1 => boundaryEnergyTextField.setVisible(true)

    }
  })

  nucleationMode = new SelectBox[String](selectBoxStyle)
  nucleationMode.setItems("Fixed amount", "Constant", "Increasing")
  nucleationMode.setSelectedIndex(0)
  nucleationMode.addListener(new ChangeListener {
    override def changed(event: ChangeListener.ChangeEvent, actor: Actor): Unit = nucleationMode.getSelectedIndex match {

      case 0 => {
        incrementRate.setVisible(false)
        nucleonsIncrement.setVisible(false)
      }
      case 1 => {
        incrementRate.setVisible(false)
        nucleonsIncrement.setVisible(false)
      }
      case 2 => {
        incrementRate.setVisible(true)
        nucleonsIncrement.setVisible(true)
      }

    }
  })

  nucleonsPlacement = new SelectBox[String](selectBoxStyle)
  nucleonsPlacement.setItems("Anywhere", "Grain boundaries")
  nucleonsPlacement.setSelectedIndex(0)

  rule4ProbabilityField.addListener(new ChangeListener {
    override def changed(event: ChangeListener.ChangeEvent, actor: Actor): Unit = if(!rule4ProbabilityField.getText.isEmpty) NaiveSeedsGrowthCell.probability = rule4ProbabilityField.getText.toInt
  })

  table.add(widthLabel).expandX
  table.add(heightLabel).expandX
  table.row
  table.add(widthField).width(uiViewport.getWorldWidth / 2)
  table.add(heightField).width(uiViewport.getWorldWidth / 2)
  table.row
  table.add(seedButton).expandX.fill
  table.add(seedField).expandX.fill
  table.row
  table.add(clearButton).expandX.fill
  table.add(nextButton).expandX.fill
  table.row
  table.add(resizeButton).expandX.fill
  table.add(toggleButton).expandX.fill
  table.row
  table.add(neighbourhoodSelection).expandX.fill
  table.row
  table.add(importButton).expandX.fill
  table.add(exportButton).expandX.fill
  table.row
  table.add(fileTypeSelection).expandX.fill
  table.row
  table.add(showBorders).expandX.fill
  table.add(grainBoundaryRenderType).expandX.fill
  table.row
  table.add(borderThicknessLabel).expandX.fill
  table.add(borderThicknessField).expandX.fill
  table.row
  table.add(inclusionAmountLabel).expandX.fill
  table.add(inclusionAmountField).expandX.fill
  table.row
  table.add(inclusionSizeLabel).expandX.fill
  table.add(inclusionSizeField).expandX.fill
  table.row
  table.add(inclusionType).expandX.fill
  table.add(addInclusionsButton).expandX.fill
  table.row
  table.add(grainShapeControl).expandX.fill
  table.add(rule4ProbabilityField).expandX.fill
  table.row
  table.add(secondStepSelection).expandX.fill
  table.add(applyButton).expandX.fill
  table.row
  table.add(mcsStepsAmount).expandX.fill
  table.add(mcsSteps).expandX.fill
  table.row
  table.add(statesAmountLabel).expandX.fill
  table.add(statesAmount).expandX.fill
  table.row
  table.add(applyMcs).expandX.fill
  table.row
  table.add(energyDistributionTypeField).fill.expandX
  table.add(energyDistributionTypeSelection).fill.expandX
  table.row
  table.add(energyTextField).expandX.fill
  table.add(boundaryEnergyTextField).expandX.fill
  table.row
  table.add(nucleationModeLabel).expandX.fill
  table.add(nucleationMode).expandX.fill
  table.row
  table.add(nucleonsAmountLabel).expandX.fill
  table.add(nucleonsAmount).expandX.fill
  table.row
  table.add(incrementRate).expandX.fill
  table.add(nucleonsIncrement).expandX.fill
  table.row
  table.add(nucleonsPlacementLabel).expandX.fill
  table.add(nucleonsPlacement).expandX.fill
  table.row
  table.add(recrystallizeButton).expandX.fill
  table.row
  table.add(renderEnergy).expandX.fill

  naiveSeedsGrowthLogic = new NaiveSeedsGrowthLogic(300, 300)
  logic = naiveSeedsGrowthLogic
  logic.getBoard.setNeighbourhood(MooreNeighbourHood, logic.getBoard.boundaryConditions.get(0))

  override def update(delta: Float): Unit = {
    super.update(delta)
    timer += delta
  }

  override def render(delta: Float): Unit = {
    super.render(delta)
    NaiveSeedsGrowthCell.grainShapeControl = grainShapeControl.isChecked
    update(delta)

    if (Logic.allProcessed) logic.isPaused = true

    if (!logic.isPaused && stepsAmount == 0) {
      logic.started = true
      logic.iterate()
    }

    if(stepsAmount > 0) {
      logic.started = true
      logic.iterate()
      stepsAmount -= 1
      logic.isPaused = true
    }

    handleInput()
  }

  override def resize(width: Int, height: Int): Unit = super.resize(width, height)

  override def dispose(): Unit = super.dispose()

  def handleInput(): Unit = {
    if (Gdx.input.justTouched) {
      var vector2 = new Vector2(Gdx.input.getX.toFloat, Gdx.graphics.getHeight.toFloat - Gdx.input.getY.toFloat)
      vector2 = cellViewport.unproject(vector2)
      if (vector2.x > 0 && vector2.y > 0) //logic.click((vector2.x / (Gdx.graphics.getHeight.toFloat / logic.getBoard.size.x.toFloat)).toInt, (vector2.y / (Gdx.graphics.getHeight.toFloat / logic.getBoard.size.y.toFloat)).toInt)
        logic.board.selectGrain((vector2.x / (Gdx.graphics.getHeight.toFloat / logic.getBoard.size.x.toFloat)).toInt, (vector2.y / (Gdx.graphics.getHeight.toFloat / logic.getBoard.size.y.toFloat)).toInt)
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.P)) logic.pause()
    if (seedButton.isPressed && Gdx.input.justTouched) logic.getBoard.seed(seedField.getText.toInt)
    if (toggleButton.isPressed && Gdx.input.justTouched) {
      logic.pause()
      if (logic.isPaused) {
        toggleButton.setText("Play")
      } else {
        toggleButton.setText("Pause")
      }
    }
    if (clearButton.isPressed && Gdx.input.justTouched) logic.getBoard.clear()
    if (nextButton.isPressed && Gdx.input.justTouched) stepsAmount = mcsSteps.getText.toInt
    if (resizeButton.isPressed && Gdx.input.justTouched) {
      logic = new NaiveSeedsGrowthLogic(widthField.getText.toInt, heightField.getText.toInt)
      logic.getBoard.setNeighbourhood(MooreNeighbourHood, logic.getBoard.boundaryConditions.get(0))
    }

    if (importButton.isPressed && Gdx.input.justTouched) logic.board.load(fileTypeSelection.getSelectedIndex)
    if (exportButton.isPressed && Gdx.input.justTouched) logic.board.save(fileTypeSelection.getSelectedIndex)
    if (addInclusionsButton.isPressed && Gdx.input.justTouched) logic.getBoard.addInclusions(inclusionType.getSelectedIndex, inclusionAmountField.getText.toInt, inclusionSizeField.getText.toInt, logic.started)
    if (applyButton.isPressed && Gdx.input.justTouched) {logic.board.secondStep(secondStepSelection.getSelectedIndex); Logic.allProcessed = false}
    if (showBorders.isPressed && Gdx.input.justTouched) logic.board.addBorders(grainBoundaryRenderType.getSelectedIndex)
    if (applyMcs.isPressed && Gdx.input.justTouched) {
      //if(!logic.started)
      logic.board.fillStates(statesAmount.getText.toInt)
      Logic.operationMode = OperationMode.SIMPLE_MCS
    }

    if (recrystallizeButton.isPressed && Gdx.input.justTouched) {

      energyDistributionTypeSelection.getSelectedIndex match {

        case 0 => logic.board.distributeEnergy(EnergyDistributionType.HOMOGENOUS, energyTextField.getText.toInt)
        case 1 => logic.board.distributeEnergy(EnergyDistributionType.HETEROGENOUS, energyTextField.getText.toInt, boundaryEnergyTextField.getText.toInt)

      }

      logic.board.placeRecrystallizationNucleons(nucleonsPlacement.getSelectedIndex, nucleonsAmount.getText.toInt)

      Logic.operationMode = OperationMode.RECRYSTALLIZATION_MCS
      logic.isPaused = false
      logic.started = false
      Logic.allProcessed = false

    }

    if(renderEnergy.isChecked) renderMode = true
    else renderMode = false

  }

}