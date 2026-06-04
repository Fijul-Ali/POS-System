<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Edit Role</title>
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

        /* --- Top Navbar (Back Action Only) --- */
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
            align-items: center;
            gap: 8px;
            text-decoration: none;
            color: var(--primary-navy);
            font-weight: 700;
            font-size: 14px;
            padding: 8px 16px;
            border-radius: 8px;
            background: #f1f5f9;
            transition: all 0.2s;
        }
        .btn-back:hover { background: #e2e8f0; transform: translateY(-1px); }

        /* --- Content Layout --- */
        .main-content {
            padding: 60px 40px;
            display: flex;
            justify-content: center;
            width: 100%;
            box-sizing: border-box;
        }

        /* --- Form Card --- */
        .form-card {
            width: 100%;
            max-width: 480px;
            background: var(--card-bg);
            padding: 40px;
            border-radius: 20px;
            border: 1px solid var(--border-color);
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.05);
        }

        .form-group { margin-bottom: 24px; }
        .form-group label {
            display: block;
            font-size: 11px;
            font-weight: 700;
            margin-bottom: 8px;
            color: var(--text-muted);
            text-transform: uppercase;
            letter-spacing: 0.05em;
        }

        .input-control {
            width: 100%;
            padding: 12px 16px;
            border: 1.5px solid var(--border-color);
            border-radius: 12px;
            font-size: 14px;
            transition: all 0.3s ease;
        }
        .input-control:focus {
            outline: none;
            border-color: var(--accent-blue);
            box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.1);
        }

        .btn-submit {
            padding: 12px 24px;
            background-color: var(--primary-navy);
            color: white;
            border: none;
            border-radius: 12px;
            font-size: 14px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.2s;
        }
        .btn-submit:hover {
            background-color: #0f172a;
            transform: translateY(-2px);
        }

        .btn-cancel {
            padding: 12px 24px;
            background: transparent;
            color: var(--text-muted);
            border: 1.5px solid var(--border-color);
            border-radius: 12px;
            font-weight: 700;
            text-decoration: none;
            font-size: 14px;
            transition: 0.2s;
        }
        .btn-cancel:hover { background: #f1f5f9; color: var(--text-main); }

        .alert-error {
            background-color: #fee2e2;
            color: #ef4444;
            padding: 12px;
            border-radius: 8px;
            font-size: 13px;
            font-weight: 600;
            margin-bottom: 20px;
            border: 1px solid #fecaca;
        }
    </style>
</head>
<body>

    <header class="top-navbar">
        <a href="${pageContext.request.contextPath}/role/list" class="btn-back">
            <span>&larr;</span> Back to Roles
        </a>
        <div style="font-size: 12px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.1em;">
            Security / Modify Role
        </div>
    </header>

    <main class="main-content">
        <div class="form-card">
            <div class="mb-4">
                <h2 style="font-weight: 800; color: var(--primary-navy); margin: 0;">Edit Role</h2>
                <p style="color: var(--text-muted); font-size: 14px;">Update security permission identifiers.</p>
            </div>

            <c:if test="${param.error eq 'true'}">
                <div class="alert-error">
                    Update failed: This role identifier is already in use.
                </div>
            </c:if>

            <c:choose>
                <c:when test="${empty role}">
                    <div class="alert-error">System Error: Role data not found.</div>
                </c:when>
                <c:otherwise>
                    <form:form action="update" method="post" modelAttribute="roleDto">
                        <form:hidden path="id" value="${role.id}"/>

                        <div class="form-group">
                            <label>Role Identifier</label>
                            <form:input path="identifier" class="input-control" value="${role.identifier}" required="true"/>
                        </div>

                <%-- Brand Description Group --%>
                <div class="form-group">
                    <label>Description</label>
                    <form:textarea path="description" class="input-control" rows="3"
                                   placeholder="Enter details about the role..." />
                    <form:errors path="description" cssClass="text-danger" style="font-size: 12px;"/>
                </div>

                        <div class="d-flex justify-content-between align-items-center mt-4">
                            <a href="${pageContext.request.contextPath}/role/list" class="btn-cancel">Cancel</a>
                            <button type="submit" class="btn-submit">Update Role</button>
                        </div>
                    </form:form>
                </c:otherwise>
            </c:choose>
        </div>
    </main>

</body>
</html>