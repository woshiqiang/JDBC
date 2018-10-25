package com.hbck.jdbc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private Button btn_register;

    private String username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                //动态加载类
                Class.forName("com.mysql.jdbc.Driver");
                //设置连接地址以及数据库名称
                String url = "jdbc:mysql://39.106.211.160:3306/test?useUnicode=true&characterEncoding=UTF-8&useSSL=true&autoReconnect=true";
                //连接数据库
                Connection conn = DriverManager.getConnection(url, "admin", "199103");
                //创建Statement对象(可执行SQL语句的对象)
                Statement stmt = conn.createStatement();
                //执行sql语句 返回一个ResultSet对象（返回数据集合）
                String sql = "SELECT UserName,UserPass FROM tb_user WHERE UserName='" + username + "' AND UserPass='" + password + "' LIMIT 1";
                ResultSet rs = stmt.executeQuery(sql);
                //遍历rs 输出数据
               /* while (rs.next()) {
                    String id = rs.getString(1);
                    String name = rs.getString(2);
                    String gender = rs.getString(3);
                    System.out.println("ID:" + id + " username：" + name + " UserPass：" + gender);
                }*/
                if (rs.next()) {
                    //登录成功,跳转到主页
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    //登录失败
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                submit();
                break;
            case R.id.btn_register:
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    private void submit() {
        username = et_username.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //新建一个线程 因为 Android 主程序中不允许直接使用网络
        new Thread(runnable).start();
    }
}
