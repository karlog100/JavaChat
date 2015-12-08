package Global;

/**
 * Created by Lynspitti on 25-11-2015.
 */

//REMEMBER: THIS IS ONLY FOR DEFINEMENT...
public interface IBackThread {
    void Initialize();
    void Process()throws InterruptedException; //DO NOT CALL THIS FROM OTHER THEN INITIALIZE
    void Dispose();
}
