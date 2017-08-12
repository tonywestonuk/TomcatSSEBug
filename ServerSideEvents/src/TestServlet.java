import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns="/TestServlet",asyncSupported=true)
public class TestServlet extends HttpServlet implements AsyncListener {
	
	private List<AsyncContext> contexts = new ArrayList<>();
	
	
	@Override
	public void init() throws ServletException {
		Timer t = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				ArrayList<AsyncContext> cpy = new ArrayList<>(contexts);
				for (AsyncContext a:cpy) {
				    PrintWriter writer;
					try {    		
						  writer = a.getResponse().getWriter();
						  writer.write("data: "+ System.currentTimeMillis() +"\n\n");
						  writer.checkError();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			           
			          
				}
			}
		};
	
		t.schedule(tt, 1000,1000);
	}

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //content type must be set to text/event-stream
        response.setContentType("text/event-stream"); 
        //cache must be set to no-cache
        response.setHeader("Cache-Control", "no-cache");     
        //encoding is set to UTF-8
        response.setCharacterEncoding("UTF-8");

        AsyncContext ac= request.startAsync();
		System.out.println(ac);

        contexts.add(ac);
        ac.addListener(this);
        
    }

	@Override
	public void onComplete(AsyncEvent arg0) throws IOException {
		System.out.println("complete");
		System.out.println(arg0.getAsyncContext());
		contexts.remove(arg0.getAsyncContext());
	}
	

	@Override
	public void onError(AsyncEvent arg0) throws IOException {
		System.out.println("error");
		System.out.println(arg0.getAsyncContext());
		contexts.remove(arg0.getAsyncContext());
	
	}

	@Override
	public void onStartAsync(AsyncEvent arg0) throws IOException {
		
	}

	@Override
	public void onTimeout(AsyncEvent arg0) throws IOException {
		
	}
    
    
}