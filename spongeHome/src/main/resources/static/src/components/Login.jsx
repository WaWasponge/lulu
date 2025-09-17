import React, { useState } from 'react';
import { Form, Input, Button, message, Card, Tabs } from 'antd';
import { UserOutlined, LockOutlined, PhoneOutlined } from '@ant-design/icons';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const [activeTab, setActiveTab] = useState('login');
  const navigate = useNavigate();

  // 手机号验证规则
  const phoneRegex = /^1[3-9]\d{9}$/;
  // 密码验证规则：至少8位，包含大小写字母和数字
  const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
  // 账号验证规则：4-16位字母数字下划线
  const accountRegex = /^[a-zA-Z0-9_]{4,16}$/;

  const onLogin = async (values) => {
    try {
      const response = await axios.post('/user/login', {
        nameAccount: values.nameAccount,
        password: values.password
      });
      
      if (response.data.code === 0) {
        // Store token in localStorage
        localStorage.setItem('token', response.data.data.token);
        message.success('登录成功！');
        navigate('/'); // Redirect to home page
      } else {
        message.error(response.data.message || '登录失败');
      }
    } catch (error) {
      message.error('登录失败，请检查网络连接');
    }
  };

  const onRegister = async (values) => {
    try {
      const response = await axios.post('/user/zhuce', {
        nameAccount: values.nameAccount,
        passWord: values.password,
        userName: values.userName,
        iphone: values.iphone
      });
      
      if (response.data.code === 0) {
        message.success('注册成功！');
        setActiveTab('login'); // Switch to login tab after successful registration
      } else {
        message.error(response.data.message || '注册失败');
      }
    } catch (error) {
      message.error('注册失败，请检查网络连接');
    }
  };

  return (
    <div style={{ 
      display: 'flex', 
      justifyContent: 'center', 
      alignItems: 'center', 
      minHeight: '100vh',
      background: '#f0f2f5'
    }}>
      <Card style={{ width: 400, boxShadow: '0 4px 8px rgba(0,0,0,0.1)' }}>
        <Tabs
          activeKey={activeTab}
          onChange={setActiveTab}
          centered
          items={[
            {
              key: 'login',
              label: '登录',
              children: (
                <Form
                  name="login"
                  onFinish={onLogin}
                  autoComplete="off"
                >
                  <Form.Item
                    name="nameAccount"
                    rules={[
                      { required: true, message: '请输入账号！' },
                      { pattern: accountRegex, message: '账号必须是4-16位字母、数字或下划线' }
                    ]}
                  >
                    <Input prefix={<UserOutlined />} placeholder="账号" />
                  </Form.Item>

                  <Form.Item
                    name="password"
                    rules={[
                      { required: true, message: '请输入密码！' },
                      { pattern: passwordRegex, message: '密码至少8位，必须包含大小写字母和数字' }
                    ]}
                  >
                    <Input.Password prefix={<LockOutlined />} placeholder="密码" />
                  </Form.Item>

                  <Form.Item>
                    <Button type="primary" htmlType="submit" block>
                      登录
                    </Button>
                  </Form.Item>
                </Form>
              ),
            },
            {
              key: 'register',
              label: '注册',
              children: (
                <Form
                  name="register"
                  onFinish={onRegister}
                  autoComplete="off"
                >
                  <Form.Item
                    name="nameAccount"
                    rules={[
                      { required: true, message: '请输入账号！' },
                      { pattern: accountRegex, message: '账号必须是4-16位字母、数字或下划线' }
                    ]}
                  >
                    <Input prefix={<UserOutlined />} placeholder="账号" />
                  </Form.Item>

                  <Form.Item
                    name="userName"
                    rules={[
                      { required: true, message: '请输入用户名！' },
                      { min: 2, max: 20, message: '用户名长度必须在2-20个字符之间' }
                    ]}
                  >
                    <Input prefix={<UserOutlined />} placeholder="用户名" />
                  </Form.Item>

                  <Form.Item
                    name="password"
                    rules={[
                      { required: true, message: '请输入密码！' },
                      { pattern: passwordRegex, message: '密码至少8位，必须包含大小写字母和数字' }
                    ]}
                  >
                    <Input.Password prefix={<LockOutlined />} placeholder="密码" />
                  </Form.Item>

                  <Form.Item
                    name="iphone"
                    rules={[
                      { required: true, message: '请输入手机号！' },
                      { pattern: phoneRegex, message: '请输入正确的手机号码' }
                    ]}
                  >
                    <Input prefix={<PhoneOutlined />} placeholder="手机号" />
                  </Form.Item>

                  <Form.Item>
                    <Button type="primary" htmlType="submit" block>
                      注册
                    </Button>
                  </Form.Item>
                </Form>
              ),
            },
          ]}
        />
      </Card>
    </div>
  );
};

export default Login; 