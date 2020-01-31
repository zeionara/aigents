/*
 * MIT License
 * 
 * Copyright (c) 2005-2020 by Anton Kolonin, Aigents®
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.webstructor.agent;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import net.webstructor.al.AL;
import net.webstructor.al.Period;
import net.webstructor.al.Writer;
import net.webstructor.comm.Socializer;
//import net.webstructor.comm.fb.FB;
//import net.webstructor.comm.goog.GApi;
//import net.webstructor.comm.vk.VK;
import net.webstructor.core.Actioner;
import net.webstructor.core.Anything;
import net.webstructor.core.Archiver;
import net.webstructor.core.Environment;
import net.webstructor.core.Updater;
import net.webstructor.core.Storager;
import net.webstructor.core.Thing;
import net.webstructor.data.GraphCacher;
import net.webstructor.data.CacherHolder;
import net.webstructor.data.Translator;
import net.webstructor.main.Logger;
import net.webstructor.peer.Conversationer;
import net.webstructor.peer.Sessioner;
import net.webstructor.data.LangPack;
import net.webstructor.self.Thinker;
import net.webstructor.util.Array;

public abstract class Body extends Anything implements Environment, Updater
{
	public final static String APPNAME = "Aigents";
	public final static String VERSION = "2.4.1";
	public final static String COPYRIGHT = "Copyright © 2020 Anton Kolonin, Aigents®.";
	public final static String ORIGINSITE = "https://aigents.com";
	
	//public final static int MAX_TRUSTED_NEWS = 500;//999; 
	//public final static int MAX_TRUSTED_LINKS = 20;//25; 
	//public final static int MAX_UNTRUSTED_LINKS = 100; 
	public final static int PEER_TRUSTS_LIMIT = 20; 
	public final static int PEER_ITEMS_LIMIT = 100; 
	public final static int MAX_UNTRUSTED_NEWS = 100;//999//TODO: make configurable
	public final static int RETROSPECTION_PERIOD_DAYS = 31;//365;//TODO: make configurable
	public final static int RETROSPECTION_RETRIES = 12 * 3;//3;//TODO: make configurable
	public final static long MAX_CHECK_CYCLE_MS = Period.DAY;//TODO: make configurable
	public final static long MIN_CHECK_CYCLE_MS = 3*Period.HOUR;//TODO: make configurable and used in Selfer.minCheckCycle
	public final static int MIN_RELEVANT_FEATURE_THRESHOLD_PERCENTS = 20;//TODO: make per-peer (custom) and per-self (default) configurable	
	
	//public final static String http_user_agent = "Aigents (Automatic intelligent internet agents; +http://www.aigents.com)";
	public final static String http_user_agent = APPNAME+"/"+VERSION+" (Automatic Intelligent Internet Agents; +"+ORIGINSITE+")";

	public static final String tcp_port = "tcp port";
	public static final String tcp_timeout = "tcp timeout";
	public static final String http_port = "http port";
	public static final String http_timeout = "http timeout";
	public static final String http_threads = "http threads";
	public static final String http_origin = "http origin";
	public static final String http_secure = "http secure";
	public static final String cookie_name = "cookie name";
	public static final String cookie_domain = "cookie domain";
	public static final String store_path = "store path";
	public static final String store_cycle = "store cycle";
	public static final String retention_period = "retention period";//LTM
	public static final String attention_period = "attention period";//STM
	public static final String email_login = "email login";
	public static final String email_password = "email password";
	public static final String email_cycle = "email cycle";
	public static final String email_retries = "email retries";	
	public static final String mail_smtp_host = "mail.smtp.host";
	public static final String mail_smtp_auth = "mail.smtp.auth";
	public static final String mail_smtp_port = "mail.smtp.port";//(25 - default, 465 - for secure ssl connection, 587 - for secure tls connection)
	public static final String mail_smtp_ssl_enable = "mail.smtp.ssl.enable";//for port 465
	public static final String mail_smtp_starttls_enable = "mail.smtp.starttls.enable";//for no port 587
	public static final String mail_store_protocol = "mail.store.protocol";
	public static final String mail_pop3s_host = "mail.pop3s.host";
	public static final String mail_pop3s_port = "mail.pop3s.port";
	public static final String mail_pop3_starttls_enable = "mail.pop3.starttls.enable";//TODO:???
	public static final String facebook_id = "facebook id";
	public static final String facebook_key = "facebook key";
	public static final String facebook_token = "facebook token";
	public static final String facebook_challenge = "facebook challenge";
	public static final String slack_id = "slack id";
	public static final String slack_key = "slack key";
	public static final String slack_token = "slack token";
	public static final String paypal_id = "paypal id";
	public static final String paypal_key = "paypal key";
	public static final String paypal_token = "paypal token";
	public static final String paypal_url = "paypal url";
	public static final String reddit_id = "reddit id";
	public static final String reddit_key = "reddit key";
	public static final String reddit_redirect = "reddit redirect";
	public static final String reddit_token = "reddit token";
	public static final String google_id = "google id";//client id or user id
	public static final String google_key = "google key";//client secret
	public static final String google_token = "google token";//access_token or temporary code
	public static final String vkontakte_id = "vkontakte id";
	public static final String vkontakte_key = "vkontakte key";
	public static final String vkontakte_token = "vkontakte token";
	public static final String telegram_id = "telegram id";
	public static final String telegram_token = "telegram token";
	public static final String telegram_offset = "telegram offset";
	public static final String steemit_id = "steemit id";
	public static final String steemit_url = "steemit url";
	public static final String golos_id = "golos id";
	public static final String golos_url = "golos url";
	public static final String ethereum_id = "ethereum id";
	public static final String ethereum_url = "ethereum url";
	public static final String ethereum_key = "ethereum key";
	public static final String ethereum_period = "ethereum period";
    
	public static final String[] strings = new String[] {
		AL.name,
		tcp_port, tcp_timeout,
		http_port, http_timeout, http_threads, http_origin, http_secure, cookie_name, cookie_domain,
		store_path, store_cycle,
		attention_period, retention_period,
		AL.email, email_login, email_password, email_cycle, email_retries,
		mail_smtp_host, mail_smtp_auth, mail_smtp_port, mail_smtp_ssl_enable, mail_smtp_starttls_enable, 
		mail_store_protocol, 
		mail_pop3s_host, mail_pop3s_port, mail_pop3_starttls_enable,
		google_id, google_key, //google_token,
		facebook_id, facebook_key, facebook_token, facebook_challenge,
		slack_id, slack_key, slack_token,
		paypal_id, paypal_key, paypal_token, paypal_url,
		reddit_id, reddit_key, reddit_token, reddit_redirect,
		vkontakte_id, vkontakte_key, vkontakte_token,
		telegram_token, telegram_offset,
		steemit_url, golos_url, ethereum_url, ethereum_key, ethereum_period,
		AL.version
	};

	public static final String[] things = new String[] {
		Storager.things_count, AL.things
	};
	
	public static final String[] properties = Array.merge(strings, things);
	
	public Sessioner sessioner;
	public Conversationer conversationer;
	public Thinker thinker;
	public Storager storager;
	public Archiver archiver;
	public Schema schema;
	public LangPack languages;
	private Logger logger = null;
	/*//TODO: remove - being plugins or configurable list of "providers"!!!
	protected FB fb = null;
	protected GApi gapi = null;
	protected VK vk = null;
	protected Socializer reddit = null;
	protected Socializer steemit = null;
	protected Socializer golos = null;
	protected Socializer ethereum = null;
	protected Socializer paypal = null;*/
	
	public net.webstructor.self.Cacher filecacher = null;
	public GraphCacher sitecacher = null;
	public CacherHolder grapher = new CacherHolder();
	protected HashMap actioners = new HashMap();
	protected HashMap<String,Socializer> socializers = new HashMap<String,Socializer>();
	
	//TODO:configuration on-line
	protected boolean console;
	
	private HashMap updaters = new HashMap();//registry of updaters, such as Emailer, SMTPer or Telegrammer
		
	public Body(boolean log, int conversationers) {		
		sessioner = new Sessioner(this);
		conversationer = new Conversationer(this,conversationers);
		thinker = new Thinker(this);
		storager = new Storager(this);//TODO:init by db/file access, but not a this body
		archiver = new Archiver(this);
		schema = new Schema(storager);

		Thing self = self();
		//http://en.wikipedia.org/wiki/List_of_TCP_and_UDP_port_numbers
		//telnet 23, http 80, 0-1023 banned
		self.setString(AL.name,"aigents");
		self.setString(tcp_port,"1123");//9999
		self.setString(tcp_timeout,"300000");//"60000");//TODO:cleanup debug relaxation
		self.setString(http_port,"1180");//8888
		self.setString(http_timeout,"60000");
		self.setString(http_threads,"2");
		self.setString(http_origin,Body.ORIGINSITE);
		self.setString(store_path,"./al.txt");
		self.setString(store_cycle,"60 sec");
		self.setString(attention_period,"7");//TODO: use "days" and Period
		self.setString(retention_period,"31");//TODO: use "days" and Period
		
		if (log)
			//logger = new Logger("selfer");
			logger = new Logger("aigents-log");
	}

	@Override
	public void register(String path, net.webstructor.data.Cacher cacher) {
		grapher.put(path, cacher);
	}
	
	@Override
	public void register(String action, Actioner actioner) {
		synchronized (actioners){
			actioners.put(action, actioner);
		}
	}
	
	@Override
	public Actioner getActioner(String action){
		synchronized (actioners){
			return (Actioner) actioners.get(action);
		}
	}
	
	@Override
	public String[] getActions(){
		synchronized (actioners){
			return (String[])actioners.keySet().toArray(new String[]{});
		}
	}
	
	public Socializer getSocializer(String name){
		synchronized (socializers) {
			return socializers.get(name);
		}
	}
	
	public Collection<Socializer> getSocializers(){
		synchronized (socializers) {
			return socializers.values();
		}
	}
	
	public void start() {
		languages = new LangPack(this);
	}
	
	//TODO: make configurable plugins for each provider
	public Socializer provider(String name){
		/*Socializer provider = "facebook".equals(name) ? (Socializer)fb :
			"vkontakte".equals(name) ? (Socializer)vk :
			"google".equals(name) ? (Socializer)gapi : 
			"steemit".equals(name) ? (Socializer)steemit : 
			"golos".equals(name) ? (Socializer)golos : 
			"ethereum".equals(name) ? (Socializer)ethereum : 
			"reddit".equals(name) ? reddit : 
			null;
		return provider;*/
		return getSocializer(name);
	}

	public Logger logger() {
		return logger;
	}
	
	public Translator translator(String language) {
		return Translator.get(language);
	}
	
	public Thing self()
	{
		try {
			Collection selfs;
			selfs = storager.getByName(AL.is,Schema.self);
			if (AL.empty(selfs)) {
				if (AL.empty(storager.getNamed(Schema.self))) //TODO: fix hack which handles aggeressive forgetting!?
					return null;
				Thing self = new Thing(storager.getNamed(Schema.self),null,null);
				self.store(storager);
				return self;
			}
			if (selfs.size() > 1)
				;//TODO: what?
			//if (selfs.size() == 1)
			return (Thing)selfs.iterator().next();
		} catch (Exception e) {
			error("No self",e);
			return null;
		}
	}
	
	public void terminate()
	{
		reply("Ended.");
		System.exit(0);
	}
	
	public void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			//TODO:log
		}
	}
	
	public void prompt() {
		//TODO: "You:"? "U:"? ">"? ;
	}

	public void reply(String text) {
		output("I:"+text);
	}

	public void output(String str) {
		if (logger != null) {
			try {
				logger.log(str, "body");
			} catch (IOException e) {
				//TODO: what?
			}
		}
		if (console)
			System.out.println(str);
	}
	
	public void debug(String str) {
		//TODO: if debug enabled
		output("D:"+str);
	}
	
	private static void print(PrintWriter pw, Throwable t){
		if (t.getMessage() != null);
			pw.println(t.getMessage());
		t.printStackTrace(pw);
		if (t.getCause() != null)
			print(pw,t.getCause());
	}
	
	public static String toString(Throwable e){
		StringWriter errors = new StringWriter();
		PrintWriter pw = new PrintWriter(errors);
		print(pw,e);
		pw.flush();
		errors.flush();
		return errors.toString();
	}
	
	public synchronized void error(String str,Throwable e) {
		if (e != null){
			/*
			StringWriter errors = new StringWriter();
			PrintWriter pw = new PrintWriter(errors);
			print(pw,e);
			pw.flush();
			errors.flush();
			str = str+":"+errors.toString();
			*/
			str = str+":"+toString(e);
		}
		output("E:"+str);
		System.err.println(str);
	}

	//@Override
	public int checkMemory(){
		//https://stackoverflow.com/questions/9732439/can-a-java-program-detect-that-its-running-low-on-heap-space
		long heapSize = Runtime.getRuntime().totalMemory();
		long heapMaxSize = Runtime.getRuntime().maxMemory();
		long heapFreeSize = Runtime.getRuntime().freeMemory();
		long remaining = Math.max(heapFreeSize, heapMaxSize - heapSize);
		return (int) Math.round(((double)(heapMaxSize-remaining))/heapMaxSize*100);
	}

	public static String notice() {
		return APPNAME + ' ' + VERSION + ' ' + COPYRIGHT;
	}
	
    public static boolean testEmail(String email) {
		return
			AL.empty(email) ||
			//allow default content population for social network users
			//email.indexOf("@facebook.com") != -1 ||
			//email.indexOf("@vk.com") != -1 || 
			//email.indexOf("@google.com") != -1 || 
			//used in unit tests
			email.equalsIgnoreCase("doe@john.org") || 
			email.equalsIgnoreCase("john@doe.org") ||
			email.equalsIgnoreCase("doe@john.com") || 
			email.equalsIgnoreCase("john@doe.com") ||
			email.equalsIgnoreCase("ali@baba.org") || 
			email.equalsIgnoreCase("baba@ali.org") ||
			email.equalsIgnoreCase("ali@baba.com") || 
			email.equalsIgnoreCase("baba@ali.com") ||
			email.equalsIgnoreCase("myname@mydomain.org") ||
			email.equalsIgnoreCase("myname@mydomain.com") ||
			email.equalsIgnoreCase("somename@somedomain.com") ||
			email.equalsIgnoreCase("somename@somedomain.org");
    }
    
	protected void finalize() throws Throwable {
		//TODO: save
	}
	
	//to be overridden by extender
	public File getFile(String path) {
		return new File(path);
		/*
		//The following code is supposed to work for Android
		String base = System.getProperty("user.dir");
		File f = new File(path);
		File p = f.getParentFile();
		File r = p == null
			? new File(base, f.getName())
			: new File(getFile(p.getPath()), f.getName());
		return r;
		*/
	}
	
	//to be overridden by extender
	public void textMessage(String phone, String subject, String message) {
		//TODO: use Texter
	}
	
	abstract public void updateStatus(boolean now);
	abstract public void updateStatus(Thing peer);
	abstract public void updateStatusRarely();
	
	//TODO: move out somewhere or remove as not used 20190224 
	public int attentionDays() {
		int days_to_retain = new Period(self().getString(Body.attention_period, "14"),Period.DAY).getDays();
		if (days_to_retain <= 0)
			days_to_retain = 7;
		return days_to_retain;
	}
	
	public String name(){
		return self().getString(AL.name,"Aigents");
	}
	
	public String signature(){
		return "-"+Writer.capitalize(name())+" at "+site();
	}
	
	public String site(){
		return self().getString(Body.http_origin,Body.ORIGINSITE);
	}
	
	public Thing getSelfPeer() {
		Collection peers = (Collection)self().get(AL.trusts);
		return AL.empty(peers) ? null : (Thing)peers.iterator().next(); 
	}
	
	public Updater register(String name, Updater updater){
		synchronized (updaters){
			updaters.put(name, updater);
		}
		return updater;
	}
	
	public boolean notifyable(Thing peer) {
		return true;//send broadcasting notifications by default
	}	
	
	public boolean update(Thing peer, String subject, String content, String signature) throws IOException{
		boolean updated = false;
		for (Iterator it = updaters.values().iterator(); it.hasNext();){
			Updater u = (Updater)it.next();
			if (!u.notifyable(peer))
				continue;
			try {
				if (AL.empty(signature))
					signature = signature();
				if (u.update(peer, subject, content, signature))
					updated = true; 
			} catch (Exception e) {
				error("Update error for "+peer.getName()+" via "+u.getClass().getSimpleName(),e);
			}
		}
		return updated;
	}
	
}
