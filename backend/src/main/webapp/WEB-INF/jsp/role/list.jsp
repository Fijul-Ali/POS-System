<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Role Security</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
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

        .btn-home {
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
        .btn-home:hover { background: #e2e8f0; transform: translateY(-1px); }

        .main-content {
            padding: 40px;
            max-width: 1200px;
            margin: 0 auto;
            width: 100%;
            box-sizing: border-box;
        }

        .data-card {
            background: var(--card-bg);
            border-radius: 16px;
            border: 1px solid var(--border-color);
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
            overflow: hidden;
        }

        .card-header-custom {
            padding: 24px 32px;
            background: white;
            border-bottom: 1px solid var(--border-color);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .table thead th {
            background-color: #f8fafc;
            text-transform: uppercase;
            font-size: 11px;
            letter-spacing: 0.05em;
            font-weight: 700;
            color: var(--text-muted);
            padding: 15px 20px;
        }

        .role-badge {
            font-weight: 700;
            color: var(--primary-navy);
            background: #eff6ff;
            padding: 6px 12px;
            border-radius: 8px;
            font-size: 13px;
        }

        /* --- INFO CIRCLE STYLES --- */
        .info-circle {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            width: 20px;
            height: 20px;
            background: #eff6ff;
            color: var(--accent-blue);
            border-radius: 50%;
            font-size: 11px;
            font-weight: 800;
            cursor: pointer;
            transition: all 0.2s;
            border: 1.5px solid #dbeafe;
            font-style: normal;
        }
        .info-circle:hover {
            background: var(--accent-blue);
            color: white;
            transform: scale(1.1);
        }

        .popover {
            border-radius: 10px;
            border: 1px solid var(--border-color);
            box-shadow: 0 10px 15px -3px rgba(0,0,0,0.1);
            padding: 5px;
        }

        .btn-op {
            border-radius: 8px;
            font-weight: 700;
            font-size: 13px;
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
            z-index: 2000;
            right: 20px;
            top: 20px;
            font-size: 14px;
            font-weight: 600;
            border-left: 5px solid var(--success-green);
        }
        #toast.show { visibility: visible; animation: slideIn 0.5s forwards; }
        @keyframes slideIn { from { transform: translateX(120%); } to { transform: translateX(0); } }
    </style>
</head>
<body>

    <div id="toast">${message}</div>

    <header class="top-navbar">
        <a href="${pageContext.request.contextPath}/" class="btn-home">
            <span>&larr;</span> Back to Home
        </a>
        <div style="font-size: 12px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.1em;">
            Security / Access Permissions
        </div>
    </header>

    <main class="main-content">
        <div class="data-card">
            <div class="card-header-custom">
                <div>
                    <h4 class="m-0" style="font-weight: 800; color: var(--primary-navy);">System Roles</h4>
                    <p class="m-0 text-muted" style="font-size: 13px;">Define and manage security roles for system access.</p>
                </div>
                <a href="${pageContext.request.contextPath}/role/add" class="btn btn-primary shadow-sm"
                   style="border-radius: 10px; font-weight: 700; padding: 10px 24px; background-color: var(--accent-blue); border: none;">
                    + Define New Role
                </a>
            </div>

            <div class="table-responsive">
                <table class="table table-hover align-middle m-0">
                    <thead>
                        <tr>
                            <th class="ps-4">Internal ID</th>
                            <th>Role Identifier</th>
                            <th class="text-end pe-4">Operations</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${fn:length(roles) > 0}">
                                <c:forEach var="role" items="${roles}">
                                    <tr>
                                        <td class="ps-4 text-muted" style="font-family: monospace; font-size: 14px;">#${role.id}</td>
                                        <td>
                                            <div class="d-flex align-items-center gap-2">
                                                <span class="role-badge">${role.identifier}</span>

                                                <%-- CLICKABLE INFO ICON (NO TITLE) --%>
                                                <i class="info-circle"
                                                   data-bs-toggle="popover"
                                                   data-bs-trigger="click"
                                                   data-bs-placement="right"
                                                   data-bs-content="${not empty role.description ? role.description : 'No description provided.'}">i</i>
                                            </div>
                                        </td>
                                        <td class="text-end pe-4">
                                            <a class="btn btn-outline-danger btn-sm px-3 btn-op"
                                               href="${pageContext.request.contextPath}/role/delete?identifier=${role.identifier}"
                                               onclick="return confirm('Deleting this role may affect user permissions. Continue?');">
                                                Remove Role
                                            </a>
                                            <a class="btn btn-outline-primary btn-sm px-3 btn-op"
                                                href="${pageContext.request.contextPath}/racks/get?identifier=${r.identifier}">Edit</a>

                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr><td colspan="3" class="text-center py-5 text-muted">No system roles defined.</td></tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        // INITIALIZE POPOVERS
        document.addEventListener('DOMContentLoaded', function () {
            var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
            var popoverList = popoverTriggerList.map(function (el) {
                return new bootstrap.Popover(el)
            });

            // Close popover when clicking outside
            document.addEventListener('click', function (e) {
                popoverTriggerList.forEach(function (el) {
                    if (!el.contains(e.target)) {
                        const instance = bootstrap.Popover.getInstance(el);
                        if (instance) instance.hide();
                    }
                });
            });
        });

        window.onload = function() {
            const msg = "${message}";
            if (msg && msg.trim() !== "") {
                const toast = document.getElementById("toast");
                toast.className = "show";
                setTimeout(() => { toast.className = ""; }, 4000);
            }
        };
    </script>
</body>
</html>