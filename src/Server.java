
import java.util.*;
import java.io.*;
import java.net.*;public class Server
{
	public static void main(String[] args)
	{
		final contain_4 c4_ovall=new contain_4();
		final List<String> banned=new ArrayList<String>();
		readBanned(banned);
		final Scanner sc=new Scanner(System.in);
		Runtime r=Runtime.getRuntime();
		System.out.println("[INFO] 可用 CPU:" + r.availableProcessors() + " Available RAM:" + r.maxMemory() / 1024 / 1024 + "now:" + new Date(System.currentTimeMillis()));
		while (true)
		{
			System.out.println("[INFO] Start port binding...");
			System.out.println("[INFO] Please type the port you want to bind(1008):");
			String r_po=sc.next();
			try
			{
				c4_ovall.setPort(Integer.parseInt(r_po));
				break;
			}
			catch (Exception e)
			{
				System.out.println("[ERROR] " + e.getMessage());
				continue;
			}
		}
		System.out.println("[INFO] Port set\n[INFO] Listening: " + c4_ovall.getPort());
		refRamT(c4_ovall);
		System.err.println("[DEBUG] I/O test\n" + c4_ovall.get());
		System.err.println("[DEBUG] I/O test complete");
		System.out.println("[SERVER] Starting control thread....");
		Thread t=new Thread(new Runnable(){

				@Override
				public void run()
				{
					while (true)
					{
						String cmd=sc.next();
						switch (cmd)
						{
							case "exit":
								System.out.println("[INFO] Stopping");
								System.exit(0);
								break;
							case "list":
								System.out.println("Listing in num");
								List<String> cur=c4_ovall.getList();
								int indicator=0;
								for (String i:cur)
								{
									System.out.println("<" + indicator + ">  " + i);
									indicator++;
								}
								break;
							case "new":
								System.out.println("Please type in the item you want to add:");
								String add=sc.next();
								c4_ovall.getList().add(add);
								saveToFile(c4_ovall);
								break;
							case "delete":
								System.out.println("Please type in the index you want delete(run 'list' to check num):");
								String del=sc.next();
								int index;
								try
								{
									index = Integer.parseInt(del);
									c4_ovall.del(index);
									saveToFile(c4_ovall);
								}
								catch (Exception e)
								{
									System.out.println("[ERROR] " + e.getMessage());
								}

								break;
							case "ref":
								refRamT(c4_ovall);
								readBanned(banned);
								System.out.println("[INFO] Refreshing variables");
								break;
							case "help":
								System.out.println("new - add item\ndelete - delete specified item\nref - refresh variables\nlist - list items in num order\nexit - kill server\nhelp - display this information\nban - ban ip\nunban - unban ip\nlist-ban - list banned ips");
								break;
							case "ban":
								System.out.println("Please type in the ip you want to ban:");
								String ban=sc.next();
								banned.add(ban);
								saveToBanFile(banned);
								break;
							case "ban-iptables":
								System.out.println("Please type in the ip you want to ban(Warning you have to unban manually):");
								String ban_1=sc.next();
								try
								{
									Runtime.getRuntime().exec("iptables -I INPUT -dport "+c4_ovall.getPort()+" -s "+ban_1+" -j DROP");
								}
								catch (IOException e)
								{}
								break;
							case "unban":
								System.out.println("Please type in the index you want delete(run 'list' to check num):");
								String un=sc.next();
								int inde;
								try
								{
									inde = Integer.parseInt(un);
									banned.remove(inde);

								}
								catch (Exception e)
								{
									System.out.println("[ERROR] " + e.getMessage());
								}
								saveToBanFile(banned);
								break;
							case "list-ban":
								System.out.println("Listing in num");
								int indicato=0;
								for (String i:banned)
								{
									System.out.println("<" + indicato + ">  " + i);
									indicato++;
								}
								break;
							default:
								System.err.println("[ERROR] Unknown command,type in 'help' to check more options");
						}
					}
					// TODO: Implement this method
				}
			});
		t.start();
		System.out.println("[INFO] Thread binded:" + t.getId());
		try
		{
			ServerSocket server=new ServerSocket(c4_ovall.getPort());
			while (true)
			{
				Socket s=server.accept();
				String ip=getIP(s.getInetAddress().toString());
				System.out.println(getTime() + "[INFO] Receive request:" + ip);
				Writer writer = new OutputStreamWriter(s.getOutputStream());
				if (banned.contains(ip))
				{
					System.out.println(getTime() + "[SERVER] Banned ip: " + ip);
					writer.write("Access denied(You are banned!)");
				}
				else
				{
					writer.write(c4_ovall.get());
					System.out.println(getTime() + "[SERVER] Transmitting data...");
				}
				writer.flush();
				writer.close();
			}
		}
		catch (Exception e)
		{System.out.println("[ERROR] "+e.getMessage());}
	}
	public static String getIP(String raw)
	{
		String[] p1=raw.split("/");
		return p1[1];
	}
	public static void readBanned(final List<String> tar)
	{
		File f=new File("/root/banned-list.txt");
		tar.clear();
		if (!f.exists())
		{
			try
			{
				System.out.println("[ERROR] 'banned-list.txt' not exist,creating file");
				f.createNewFile();
				System.out.println("[INFO] File created");
			}
			catch (IOException e)
			{}
		}
		else
		{
			read(f, new line_op(){

					@Override
					public void onLineR(String l)
					{
						tar.add(l);
						// TODO: Implement this method
					}
				});
		}
	}
	static class contain_4
	{
		private String text;
		private List<String> list;
		private int port=1008;
		public void setString(String text)
		{
			this.text = text;
		}
		public void setList(List<String> l1)
		{
			list = l1;
		}
		public void setPort(int p){
			port=p;
		}
		public int getPort(){
			return port;
		}
		public void add(String a)
		{
			list.add(a);
		}
		public void del(int num)
		{
			list.remove(num);
		}
		public List<String> getList()
		{
			return list;
		}
		public String get()
		{
			return text;
		}
	}
	public static void refRamT(contain_4 tar)
	{
		final List<String> temp=new LinkedList<String>();
		tar.setString(read(new File("/root/cachelist.txt"), new line_op(){

							  @Override
							  public void onLineR(String l)
							  {
								  temp.add(l);
								  // TODO: Implement this method
							  }
						  }));
		tar.setList(temp);
	}
	public static void saveToFile(contain_4 tar)
	{
		String c="";
		List<String> cur=tar.getList();
		for (String i:cur)
		{
			c += i + "\n";
		}
		write(new File("/root/cachelist.txt"), c);
		System.out.println("[INFO] Saved to file /root/cachelist.txt");
	}
	public static void saveToBanFile(List<String> sav)
	{
		String c="";
		List<String> cur=sav;
		for (String i:cur)
		{
			c += i + "\n";
		}
		write(new File("/root/banned-list.txt"), c);
		System.out.println("[INFO] Saved to file /root/banned-list.txt");
	}
	public static void write(File file, String content)
	{
		if (!file.exists())
		{
			try
			{
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			catch (Exception e)
			{}
		}
		try
		{
			FileOutputStream fos=new FileOutputStream(file);
			fos.write(content.getBytes());
			fos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static String getTime()
	{
		Date d=new Date(System.currentTimeMillis());
		return "[" + d.toString() + "]";
	}
	public static String read(File file, line_op op)
	{
		String line;
		String content="";
		try
		{
			FileInputStream fis=new FileInputStream(file);
			if (fis != null)
			{
				InputStreamReader reader=new InputStreamReader(fis);
				BufferedReader buffreader = new BufferedReader(reader);
				while ((line = buffreader.readLine()) != null && !line.equals(""))
				{
					content += line;
					content = content + "\n";
					op.onLineR(line);
				}
				fis.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return content;
	}
	interface line_op
	{
		public void onLineR(String l);
	}
}
