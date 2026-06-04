<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Staff Registration</title>
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
            padding: 20px;
            box-sizing: border-box;
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

        @keyframes slideIn { from { transform: translateX(120%); } to { transform: translateX(0); } }
        @keyframes fadeOut { from { opacity: 1; transform: translateX(0); } to { opacity: 0; transform: translateX(120%); } }

        .register-card {
            width: 100%;
            max-width: 480px;
            background: var(--card-bg);
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.05);
            border: 1px solid var(--border-color);
            box-sizing: border-box;
        }

        .header { margin-bottom: 30px; text-align: center; }
        .header h2 { margin: 0; font-size: 26px; font-weight: 800; letter-spacing: -0.03em; color: var(--primary-navy); }
        .header p { margin: 10px 0 0 0; font-size: 14px; color: var(--text-muted); font-weight: 500; }

        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; font-size: 12px; font-weight: 700; margin-bottom: 8px; color: var(--text-main); text-transform: uppercase; letter-spacing: 0.05em; }

        .input-control {
            width: 100%;
            padding: 12px 16px;
            border: 1.5px solid var(--border-color);
            border-radius: 12px;
            font-size: 14px;
            background-color: #fcfcfc;
            box-sizing: border-box;
            transition: all 0.3s ease;
        }

        .input-control:focus { outline: none; border-color: var(--accent-blue); background-color: #fff; box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.1); }

        .checkbox-group {
            border: 1.5px solid var(--border-color);
            border-radius: 12px;
            padding: 12px;
            background-color: #fcfcfc;
            max-height: 140px;
            overflow-y: auto;
        }

        .checkbox-group span { display: flex; align-items: center; margin-bottom: 8px; font-size: 14px; cursor: pointer; }
        .checkbox-group input[type="checkbox"] { width: 18px; height: 18px; margin-right: 10px; accent-color: var(--accent-blue); cursor: pointer; }

        .error-msg { color: var(--error-red); font-size: 12px; margin-top: 6px; font-weight: 600; display: block; }

        .btn-submit {
            width: 100%;
            padding: 14px;
            background-color: var(--primary-navy);
            color: white;
            border: none;
            border-radius: 12px;
            font-size: 15px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.2s ease;
            margin-top: 10px;
        }

        .btn-submit:hover { background-color: #0f172a; transform: translateY(-2px); box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1); }

        .card-footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid var(--border-color); text-align: center; font-size: 14px; }
        .card-footer a { color: var(--accent-blue); text-decoration: none; font-weight: 700; }
        .card-footer a:hover { text-decoration: underline; }

        .v-hook { position: absolute; opacity: 0; width: 0; height: 0; pointer-events: none; }

        @media (max-width: 480px) {
            .register-card { padding: 30px 20px; }
            .header h2 { font-size: 22px; }
        }
    </style>
</head>
<body>

<div id="toast" role="alert"><span>⚠️</span> ${message}</div>

<main class="register-card">
    <header class="header">
        <h2>Staff Registration</h2>
        <p>System Onboarding & Role Assignment</p>
    </header>

    <form:form action="register" method="post" modelAttribute="userDto">
        <div class="form-group">
            <label>Full Name</label>
            <form:input path="name" class="input-control" placeholder="John Doe" required="required"/>
            <form:errors path="name" cssClass="error-msg" />
        </div>

        <div class="form-group">
            <label>Email Address</label>
            <form:input path="username" type="email" class="input-control" placeholder="name@retail.com" required="required"/>
            <form:errors path="username" cssClass="error-msg" />
        </div>

        <div class="form-group">
            <label>Assign System Roles</label>
            <div class="checkbox-group">
                <form:checkboxes path="roles" items="${roles}" itemValue="identifier"
                                 itemLabel="identifier" element="span"
                                 onchange="validateRoles()"/>
                <input type="checkbox" id="roleWatcher" class="v-hook" required title="Select at least one role">
            </div>
            <form:errors path="roles" cssClass="error-msg" />
        </div>

        <div class="form-group">
            <label>Phone Number</label>
            <form:input path="phoneNo" type="tel" class="input-control" placeholder="10 Digit Mobile No"
                        required="required" maxlength="10" pattern="[0-9]{10}"
                        oninput="this.value = this.value.replace(/[^0-9]/g, '');" />
            <form:errors path="phoneNo" cssClass="error-msg" />
        </div>

        <div class="form-group">
            <label>Secure Password</label>
            <form:password path="password" class="input-control" placeholder="Min 6 characters"
                           minlength="6" required="required"/>
            <form:errors path="password" cssClass="error-msg" />
        </div>

        <button type="submit" class="btn-submit">Register User</button>
    </form:form>

    <footer class="card-footer">
        <span>Already have an account?</span>
        <a href="${pageContext.request.contextPath}/login">Sign In</a>
    </footer>
</main>

<script>
    function validateRoles() {
        const checkboxes = document.getElementsByName('roles');
        const watcher = document.getElementById('roleWatcher');
        watcher.required = !Array.from(checkboxes).some(c => c.checked);
    }

    window.onload = function() {
        validateRoles();
        const msg = "${message}";
        if (msg && msg.trim() !== "") {
            const toast = document.getElementById("toast");
            toast.className = "show";
            setTimeout(() => { toast.className = toast.className.replace("show", ""); }, 4000);
        }
    };
</script>

</body>
</html>