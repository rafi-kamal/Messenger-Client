package messenger.client.view;

public interface GUI {

	/**
	 * Notifies the user that an exception has been occurred 
	 * @param message
	 */
	public abstract void displayErrorMessage(final String message);

	public abstract void displayInfoMessage(final String message);

	public abstract void setVisible(boolean b);

	public abstract void close();

}