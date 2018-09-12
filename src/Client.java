import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public int port=8080;
    Socket socket=null;

    public static void main(String[] args) {
        new Client();
    }

    public Client(){
        try{
            socket =new Socket("127.0.0.1",port);
            new ClientThread().start();

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //控制台输出获取的消息
            String msg;
            //当从服务器有消息时，就输出
            while ((msg = br.readLine()) != null) {
                System.out.println(msg);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //客户端线程
    class ClientThread extends Thread{

        @Override
        public void run() {
            try{
                BufferedReader re=new BufferedReader(new InputStreamReader(System.in));
                PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);
                String msg;
                while (true){
                    msg=re.readLine();
                    pw.println(msg);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
