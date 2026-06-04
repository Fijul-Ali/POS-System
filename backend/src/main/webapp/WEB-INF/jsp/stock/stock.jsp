<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Edit Inventory</title>
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

        .main-content {
            padding: 60px 40px;
            display: flex;
            justify-content: center;
            width: 100%;
            box-sizing: border-box;
        }

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

        .input-control[readonly] {
            background-color: #f1f5f9;
            color: #94a3b8;
            cursor: not-allowed;
            border-style: dashed;
        }

        .input-control:focus:not([readonly]) {
            outline: none;
            border-color: var(--accent-blue);
            box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.1);
        }

        /* --- Scrollable Checkbox Group --- */
        .checkbox-group {
            border: 1.5px solid var(--border-color);
            border-radius: 12px;
            padding: 8px;
            background-color: #fff;
            max-height: 180px;
            overflow-y: auto;
        }

        .checkbox-group div {
            display: flex;
            align-items: center;
            padding: 8px 12px;
            border-radius: 8px;
            transition: background 0.2s;
        }
        .checkbox-group div:hover { background-color: #f1f5f9; }

        .checkbox-group input[type="checkbox"] {
            width: 18px;
            height: 18px;
            margin-right: 12px;
            accent-color: var(--accent-blue);
            cursor: pointer;
        }

        .checkbox-group label {
            margin: 0;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
            color: var(--text-main);
            text-transform: none;
        }

        .checkbox-group::-webkit-scrollbar { width: 6px; }
        .checkbox-group::-webkit-scrollbar-thumb { background: #cbd5e1; border-radius: 10px; }

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
        .btn-submit:hover { background-color: #0f172a; transform: translateY(-2px); }

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

        #toast {
            visibility: hidden; min-width: 280px; background-color: var(--primary-navy); color: #fff;
            text-align: center; border-radius: 12px; padding: 16px; position: fixed;
            z-index: 2000; right: 20px; top: 20px; font-size: 14px; font-weight: 600;
            border-left: 5px solid var(--accent-blue);
        }
        #toast.show { visibility: visible; animation: slideIn 0.5s forwards; }
        @keyframes slideIn { from { transform: translateX(120%); } to { transform: translateX(0); } }
    </style>
</head>
<body>

    <div id="toast">${message}</div>

    <header class="top-navbar">
        <a href="${pageContext.request.contextPath}/stock/list" class="btn-back">
            <span>&larr;</span> Back to Inventory
        </a>
        <div style="font-size: 12px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.1em;">
            Inventory / Modify Stock
        </div>
    </header>

    <main class="main-content">
        <div class="form-card">
            <div class="mb-4">
                <h2 style="font-weight: 800; color: var(--primary-navy); margin: 0;">Edit Entry</h2>
                <p style="color: var(--text-muted); font-size: 14px;">Update stock levels and storage locations.</p>
            </div>

            <form:form id="editStockForm" action="${pageContext.request.contextPath}/stock/update" method="POST" modelAttribute="stockDto">

                <%-- Crucial for update logic --%>
                <form:hidden path="id" />
                <form:hidden path="identifier" />

                <div class="form-group">
                    <label>Stock Identifier (Read-Only)</label>
                    <form:input path="identifier" class="input-control" readonly="true" disabled="true" />
                </div>

                <div class="form-group">
                    <label>Current Quantity</label>
                    <form:input path="quantity" type="number" min="0" class="input-control" required="true"
                                onkeypress="return event.charCode >= 48"/>
                </div>

                <div class="form-group">
                    <label>Assigned Warehouses</label>
                    <div class="checkbox-group">
                        <c:choose>
                            <c:when test="${not empty warehouses}">
                                <form:checkboxes path="warehouses"
                                                items="${warehouses}"
                                                itemValue="identifier"
                                                itemLabel="identifier"
                                                element="div" />
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted p-2 m-0" style="font-size: 13px;">No warehouses available.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <%-- Brand Description Group --%>
                <div class="form-group">
                    <label>Description</label>
                    <form:textarea path="description" class="input-control" rows="3"
                                   placeholder="Enter details about the stock..." />
                    <form:errors path="description" cssClass="text-danger" style="font-size: 12px;"/>
                </div>

                <div class="d-flex justify-content-between align-items-center mt-4">
                    <a href="${pageContext.request.contextPath}/stock/list" class="btn-cancel">Cancel</a>
                    <button type="submit" class="btn-submit">Save Changes</button>
                </div>
            </form:form>
        </div>
    </main>

    <script>
        window.onload = function() {
            var msg = "${message}";
            if (msg && msg.trim() !== "") {
                var x = document.getElementById("toast");
                x.className = "show";
                setTimeout(function(){ x.className = ""; }, 4000);
            }

            // Validation for Edit form
            const form = document.getElementById('editStockForm');
            form.onsubmit = function() {
                const checkedCount = document.querySelectorAll('input[name="warehouses"]:checked').length;
                if (checkedCount === 0) {
                    alert("A stock entry must be assigned to at least one warehouse.");
                    return false;
                }
                return true;
            };
        };
    </script>

</body>
</html>