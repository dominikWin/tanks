package core;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import core.ui.UserInterface;
import core.util.Logger;

public class Game {

	public enum GameState {
		LOADING, MAIN_MENU, PLAYING, PAUSED, DEAD;
	}

	public static int WIDTH;

	public static int HEIGHT;
	private static World world;

	private static UserInterface userInterface;

	public static GameState gameState;

	private static void createDisplay() {
		try {
			Display.setDisplayModeAndFullscreen(new DisplayMode(1280, 720));
			Display.setFullscreen(false);
			Display.setVSyncEnabled(true);
			Display.setResizable(false);

			Game.WIDTH = Display.getDisplayMode().getWidth();
			Game.HEIGHT = Display.getDisplayMode().getHeight();
			Logger.log("Display created with display mode:" + Display.getDisplayMode());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		;
		try {
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

	}

	private static void exit() {
		exit(0);
	}

	static void exit(int status) {
		Display.destroy();
		Logger.close();
		System.exit(status);
	}

	private static void gameLoop() {
		gameState = GameState.MAIN_MENU;
		long lastRunTime = 0;
		double time = 0;
		while (!Display.isCloseRequested()) {
			long startTime = System.nanoTime();

			{// Scope declared to emphasize position between start and end time
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				Input.update();
				update(time);
				render();
				Display.update();
			}

			long endTime = System.nanoTime();
			lastRunTime = endTime - startTime;
			time = lastRunTime / 1000000000d;
		}
		exit();
	}

	private static void render() {
		glTranslated(-(world.getPlayer().location.getX() - Game.WIDTH / 2),
				-(world.getPlayer().location.getY() - Game.HEIGHT / 2), 0);
		renderWithShift();
		glTranslated(world.getPlayer().location.getX() - Game.WIDTH / 2,
				world.getPlayer().location.getY() - Game.HEIGHT / 2, 0);
		renderWithoutShift();
	}

	public static UserInterface getUserInterface() {
		return userInterface;
	}

	public static World getWorld() {
		return world;
	}

	private static void glInit() {
		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	private static void init() {
		Logger.log("Starting TankGame");
		Logger.log("Intitating gameState");
		gameState = GameState.LOADING;
		Logger.log("Creating world");
		world = new World();
		Logger.log("Initializing OpenGL");
		glInit();
		Logger.log("Initializing world");
		world.init();
		Logger.log("Creating user interface");
		userInterface = new UserInterface();
	}

	public static void main(String[] args) {
		createDisplay();
		init();
		gameLoop();
	}

	private static void renderWithoutShift() {
		userInterface.render();
	}

	private static void renderWithShift() {
		if (gameState == GameState.PLAYING)
			world.render();
	}

	public static void setUserInterface(UserInterface userInterface) {
		Game.userInterface = userInterface;
	}

	public static void setWorld(World world) {
		Game.world = world;
	}

	private static void update(double time) {
		if (gameState == GameState.PLAYING)
			world.update(time);
		userInterface.update(time);
	}
}
