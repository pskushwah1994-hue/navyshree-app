package com.navyshreecomputereducation.app;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    final int NAVY = Color.rgb(11,61,145), GOLD = Color.rgb(244,180,0), BG = Color.rgb(247,249,252), DARK = Color.rgb(23,32,51);
    LinearLayout root, content;
    android.content.SharedPreferences prefs;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        prefs=getSharedPreferences("student",MODE_PRIVATE);
        FirebaseHelper.signInAnonymously(this::showHome, this::showHome);
    }
    TextView tv(String s,float size,int color, boolean bold){ TextView t=new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color); t.setPadding(8,8,8,8); if(bold)t.setTypeface(null,1); return t; }
    Button btn(String s){ Button b=new Button(this); b.setText(s); b.setTextSize(15); b.setAllCaps(false); b.setTextColor(Color.WHITE); b.setBackgroundColor(NAVY); LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,58); p.setMargins(0,6,0,6); b.setLayoutParams(p); return b; }
    void base(String title){
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(BG);
        TextView bar=tv(title,20,Color.WHITE,true); bar.setGravity(Gravity.CENTER_VERTICAL); bar.setPadding(18,0,12,0); bar.setBackgroundColor(NAVY); root.addView(bar,new LinearLayout.LayoutParams(-1,62));
        ScrollView sc=new ScrollView(this); content=new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); content.setPadding(18,18,18,28); sc.addView(content); root.addView(sc,new LinearLayout.LayoutParams(-1,0,1));
        setContentView(root);
    }
    void showHome(){
        base("Navyshree Computer Education");
        ImageView logo=new ImageView(this); logo.setImageResource(R.drawable.navyshree_logo); logo.setAdjustViewBounds(true); content.addView(logo,new LinearLayout.LayoutParams(-1,190));
        TextView welcome=tv("Learn • Practice • Succeed",18,GOLD,true); welcome.setGravity(Gravity.CENTER); content.addView(welcome);
        String student=prefs.getString("name",""); if(!student.isEmpty()){ TextView hi=tv("Welcome, "+student+" 👋",18,DARK,true); hi.setGravity(Gravity.CENTER); content.addView(hi); }
        Button reg=btn(student.isEmpty()?"👨‍🎓 Student Registration":"👨‍🎓 Update Student Profile"); reg.setOnClickListener(v->registration()); content.addView(reg);
        Button courses=btn("📚 Courses & Study Material"); courses.setOnClickListener(v->courses()); content.addView(courses);
        Button test=btn("📝 Online Test / Quiz"); test.setOnClickListener(v->quiz()); content.addView(test);
        Button notices=btn("📢 Notices & Announcements"); notices.setOnClickListener(v->notices()); content.addView(notices);
        Button schedule=btn("📅 Class / Exam Schedule"); schedule.setOnClickListener(v->schedule()); content.addView(schedule);
        Button results=btn("📄 Result & Certificate"); results.setOnClickListener(v->results()); content.addView(results);
        Button contact=btn("📞 Contact Admin"); contact.setOnClickListener(v->contact()); content.addView(contact);
        TextView admin=tv("Admin: Pravesh Kushwah\nContact: 8770341874",14,DARK,false); admin.setGravity(Gravity.CENTER); content.addView(admin);
    }
    void registration(){
        base("Student Registration");
        content.addView(tv("Enter your details",20,NAVY,true));
        EditText name=new EditText(this); name.setHint("Student Name"); name.setText(prefs.getString("name","")); content.addView(name);
        EditText mobile=new EditText(this); mobile.setHint("Mobile Number"); mobile.setInputType(2); mobile.setText(prefs.getString("mobile","")); content.addView(mobile);
        EditText course=new EditText(this); course.setHint("Course (e.g. DCA / Tally / MS Office)"); course.setText(prefs.getString("course","")); content.addView(course);
        Button save=btn("Save Profile"); save.setOnClickListener(v->{ if(name.getText().toString().trim().isEmpty()){name.setError("Name required");return;} String n=name.getText().toString().trim();
                String m=mobile.getText().toString().trim();
                String c=course.getText().toString().trim();
                prefs.edit().putString("name",n).putString("mobile",m).putString("course",c).apply();
                FirebaseHelper.saveStudent(n,m,c,
                    () -> { Toast.makeText(this,"Profile saved to Firebase",Toast.LENGTH_SHORT).show(); showHome(); },
                    () -> { Toast.makeText(this,"Profile saved on this phone. Firebase sync is unavailable.",Toast.LENGTH_LONG).show(); showHome(); }); }); content.addView(save);
        backButton();
    }
    void courses(){
        base("Courses & Study Material");
        String[][] data={{"DCA – Diploma in Computer Applications","Computer fundamentals, Windows, MS Word, Excel, PowerPoint and Internet."},{"Tally Prime","Accounting basics, company creation, vouchers, GST concepts and reports."},{"MS Office","Word, Excel and PowerPoint with practical exercises."},{"Typing & Computer Basics","English/Hindi typing practice, shortcuts and daily computer skills."}};
        for(String[] d:data){ content.addView(tv("▸ "+d[0],18,NAVY,true)); content.addView(tv(d[1],15,DARK,false)); Button b=btn("Open Study Material"); b.setOnClickListener(v->Toast.makeText(this,"Study material will be added by the institute.",Toast.LENGTH_LONG).show()); content.addView(b); }
        backButton();
    }
    void quiz(){
        base("Online Test / Quiz");
        final String[] qs={"Which key is used to refresh a webpage?","Which software is mainly used for presentations?","What does CPU stand for?","Which is a spreadsheet application?","What is the shortcut for Copy?"};
        final String[][] opts={{"F5","F1","F2","Esc"},{"PowerPoint","Paint","Notepad","Calculator"},{"Central Processing Unit","Computer Perso
