public class main {
    public static void main(String[] args) {

        AffableThread mSecondThread = new AffableThread();    //Создание потока
        mSecondThread.start();                                  //Запуск потока

        System.out.println("Главный поток завершён...");
    }
}
