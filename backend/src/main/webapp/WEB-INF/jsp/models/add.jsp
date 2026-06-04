<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Add Product Model</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        :root {
            --primary-navy: #1e293b;
            --accent-blue: #2563eb;
            --bg-body: #f8fafc;
            --card-bg: #ffffff;
            --text-main: #1e293b;
            --text-muted: #64748b;
            --border-color: #e2e8f0;
        }

        body {
            margin: 0;
            font-family: 'Inter', system-ui, -apple-system, sans-serif;
            background-color: var(--bg-body);
            color: var(--text-main);
            min-height: 100vh;
        }

        .top-navbar {
            background: rgba(255, 255, 255, 0.9);
            backdrop-filter: blur(8px);
            height: 70px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 40px;
            border-bottom: 1px solid var(--border-color);
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .btn-back {
            display: flex;
            align-items: center; gap: 8px;
            text-decoration: none; color: var(--primary-navy);
            font-weight: 700; font-size: 14px; padding: 8px 16px;
            border-radius: 8px; background: #f1f5f9; transition: all 0.2s;
        }
        .btn-back:hover { background: #e2e8f0; transform: translateY(-1px); }

        .main-content {
            padding: 60px 40px;
            display: flex; justify-content: center; width: 100%;
        }

        .form-card {
            width: 100%; max-width: 480px;
            background: var(--card-bg); padding: 40px;
            border-radius: 20px; border: 1px solid var(--border-color);
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.05);
        }

        .form-group { margin-bottom: 24px; }
        .form-group label {
            display: block; font-size: 11px; font-weight: 700;
            margin-bottom: 8px; color: var(--text-muted);
            text-transform: uppercase; letter-spacing: 0.05em;
        }

        .input-control {
            width: 100%; padding: 12px 16px;
            border: 1.5px solid var(--border-color); border-radius: 12px;
            font-size: 14px; transition: all 0.3s ease;
        }
        .input-control:focus {
            outline: none; border-color: var(--accent-blue);
            box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.1);
        }

        .btn-submit {
            width: 100%; padding: 14px;
            background-color: var(--primary-navy); color: white;
            border: none; border-radius: 12px;
            font-size: 15px; font-weight: 700; cursor: pointer;
            transition: all 0.2s; margin-top: 10px;
        }
        .btn-submit:hover { background-color: #0f172a; transform: translateY(-2px); }
    </style>
</head>
<body>

    <header class="top-navbar">
        <a href="${pageContext.request.contextPath}/models/list" class="btn-back">
            <span>&larr;</span> Back to Directory
        </a>
        <div style="font-size: 12px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.1em;">
            Inventory / New Model
        </div>
    </header>

    <main class="main-content">
        <div class="form-card">
            <div class="mb-4">
                <h2 style="font-weight: 800; color: var(--primary-navy); margin: 0;">Add Model</h2>
                <p style="color: var(--text-muted); font-size: 14px;">Define specific versions for your brands.</p>
            </div>

            <form:form id="modelForm" method="POST" action="add" modelAttribute="modelsDto">

                <%-- Model Identifier --%>
                <div class="form-group">
                    <label>Model Identifier</label>
                    <form:input path="identifier" class="input-control" placeholder="e.g. GALAXY-S24, XPS-13" required="true"/>
                    <form:errors path="identifier" cssClass="text-danger" style="font-size: 12px;"/>
                </div>

                <%-- Brand Description Group --%>
                <div class="form-group">
                    <label>Description</label>
                    <form:textarea path="description" class="input-control" rows="3"
                                   placeholder="Enter details about the model..." />
                    <form:errors path="description" cssClass="text-danger" style="font-size: 12px;"/>
                </div>


                <div class="mt-2" style="font-size: 11px; color: var(--text-muted); line-height: 1.5;">
                    Note: Status will be <strong>Active</strong> by default.
                </div>

                <button type="submit" class="btn-submit">Save Model</button>
            </form:form>
        </div>
    </main>

</body>
</html>