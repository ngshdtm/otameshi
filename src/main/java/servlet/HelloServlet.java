package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HelloServlet", urlPatterns = { "/hello" }, asyncSupported = true)
public class HelloServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {

		AsyncContext asyncContext = req.startAsync();
		asyncContext.addListener(new AsyncListener() {

			public void onTimeout(AsyncEvent arg0) throws IOException {
				System.out.println("timeout");
				arg0.getAsyncContext().complete();
			}

			public void onStartAsync(AsyncEvent arg0) throws IOException {
				System.out.println("start");
			}

			public void onError(AsyncEvent arg0) throws IOException {
			}

			public void onComplete(AsyncEvent arg0) throws IOException {
				System.out.println("complete");
			}
		});
		asyncContext.setTimeout(10000);
		asyncContext.start(new Hogee(asyncContext));

		res.setContentType("text/html");
		res.setCharacterEncoding("UTF-8");
		res.setStatus(HttpServletResponse.SC_OK);

		String str = "<h1>Hello Servlet!</h1>";

		PrintWriter pw = res.getWriter();
		pw.write(str);
		pw.close();

	}

	class Hogee implements Runnable {
		AsyncContext ctx;

		public Hogee(AsyncContext ctx) {
			super();
			this.ctx = ctx;
		}

		public void run() {
			try {
				TimeUnit.SECONDS.sleep(5);
				System.out.println("hoge");
				ctx.complete();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}