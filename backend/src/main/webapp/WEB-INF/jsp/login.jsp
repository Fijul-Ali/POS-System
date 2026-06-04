<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Secure Access</title>
    <style>
        :root {
            --primary-navy: #1e293b;
            --accent-blue: #2563eb;
            --success-green: #10b981;
            --error-red: #ef4444;
            --bg-body: #f8fafc;
            --card-bg: #ffffff;
            --text-main: #1e293b;
            --text-muted: #64748b;
            --border-color: #e2e8f0;
        }

        body {
            font-family: 'Inter', system-ui, -apple-system, sans-serif;
            background-color: var(--bg-body);
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            color: var(--text-main);
        }

        #toast {
            visibility: hidden;
            min-width: 280px;
            background-color: var(--primary-navy);
            color: #fff;
            text-align: center;
            border-radius: 12px;
            padding: 16px;
            position: fixed;
            z-index: 1000;
            right: 20px;
            top: 20px;
            font-size: 14px;
            font-weight: 600;
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1);
            border-left: 5px solid var(--error-red);
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
        }

        #toast.show {
            visibility: visible;
            animation: slideIn 0.5s forwards, fadeOut 0.5s 2.5s forwards;
        }

        @keyframes slideIn {
            from { transform: translateX(120%); }
            to { transform: translateX(0); }
        }

        @keyframes fadeOut {
            from { opacity: 1; transform: translateX(0); }
            to { opacity: 0; transform: translateX(120%); }
        }

        .login-card {
            width: 90%;
            max-width: 420px;
            background: var(--card-bg);
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.05), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
            border: 1px solid var(--border-color);
            box-sizing: border-box;
        }

        .brand-header {
            margin-bottom: 35px;
            text-align: center;
        }

        .brand-header h2 {
            margin: 0;
            font-size: 28px;
            font-weight: 800;
            letter-spacing: -0.03em;
            color: var(--primary-navy);
        }

        .brand-header p {
            margin: 10px 0 0 0;
            font-size: 15px;
            color: var(--text-muted);
            font-weight: 500;
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            display: block;
            font-size: 12px;
            font-weight: 700;
            margin-bottom: 10px;
            color: var(--text-main);
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }

        .input-control {
            width: 100%;
            padding: 14px 16px;
            border: 1.5px solid var(--border-color);
            border-radius: 12px;
            font-size: 15px;
            background-color: #fcfcfc;
            box-sizing: border-box;
            transition: all 0.3s ease;
        }

        .input-control:hover {
            border-color: #cbd5e1;
        }

        .input-control:focus {
            outline: none;
            border-color: var(--accent-blue);
            background-color: #fff;
            box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.1);
        }

        .btn-signin {
            width: 100%;
            padding: 15px;
            background-color: var(--primary-navy);
            color: white;
            border: none;
            border-radius: 12px;
            font-size: 16px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.2s ease;
            margin-top: 10px;
        }

        .btn-signin:hover {
            background-color: #0f172a;
            transform: translateY(-2px);
            box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
        }

        .btn-signin:active {
            transform: translateY(0);
        }

        .card-footer {
            margin-top: 35px;
            padding-top: 25px;
            border-top: 1px solid var(--border-color);
            text-align: center;
        }

        .card-footer span {
            font-size: 14px;
            color: var(--text-muted);
        }

        .link-register {
            font-size: 14px;
            font-weight: 700;
            color: var(--accent-blue);
            text-decoration: none;
            margin-left: 5px;
        }

        .link-register:hover {
            text-decoration: underline;
        }

        /* Responsive Design Breakpoint */
        @media (max-width: 480px) {
            .login-card {
                padding: 30px 20px;
            }
            .brand-header h2 {
                font-size: 24px;
            }
        }

        .version-tag {
            position: absolute;
            bottom: 20px;
            font-size: 11px;
            font-weight: 600;
            color: #94a3b8;
            letter-spacing: 0.1em;
            text-transform: uppercase;
        }
    </style>
</head>
<body>

<div id="toast" role="alert">
    <span>✕</span> Invalid E-mail or Password
</div>

<main class="login-card">
    <header class="brand-header">
        <h2>System Login</h2>
        <p>Retail Management & POS Interface</p>
    </header>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group">
            <label for="username">E-mail Address</label>
            <input type="email" id="username" name="username" class="input-control"
                   placeholder="e.g. admin@retail.com" required autofocus
                   autocomplete="username">
        </div>

        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" class="input-control"
                   placeholder="••••••••" required autocomplete="current-password">
        </div>

        <button type="submit" class="btn-signin">Sign In to Dashboard</button>
    </form>

    <footer class="card-footer">
        <span>New staff member?</span>
        <a href="${pageContext.request.contextPath}/register" class="link-register">Create Account</a>
    </footer>
</main>

<script>
    window.addEventListener('DOMContentLoaded', (event) => {
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has('error')) {
            const toast = document.getElementById("toast");
            toast.className = "show";
            setTimeout(() => {
                toast.className = toast.className.replace("show", "");
            }, 4000);
        }
    });
</script>

</body>
</html>
