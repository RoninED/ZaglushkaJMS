public class main {
    public static void main(String[] args) {

        AffableThread mFirstThread = new AffableThread("whazzzup", "Hi mark");
        AffableThread mSecondThread = new AffableThread("Hi mark", "whazzzup");    //Создание потока
         mSecondThread.start();                                  //Запуск потока
        mFirstThread.start();

        System.out.println("Главный поток завершён...");
    }
}
