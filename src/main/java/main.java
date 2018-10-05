public class main {
    public static void main(String[] args) {

        AffableThread mSecondThread = new AffableThread("Hi mark", "whazzzup");    //Создание потока
        mSecondThread.start();                                  //Запуск потока

        System.out.println("Главный поток завершён...");
    }
}
