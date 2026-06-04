<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Customer Directory</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        :root {
            --primary-navy: #1e293b;
            --accent-blue: #2563eb;
            --bg-body: #f8fafc;
            --card-bg: #ffffff;
            --text-main: #1e293b;
            --text-muted: #64748b;
            --border-color: #e2e8f0;
            --success-green: #10b981;
            --danger-red: #ef4444;
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
            max-width: 1400px;
            margin: 0 auto;
            width: 100%;
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

        .customer-avatar {
            width: 38px;
            height: 38px;
            background: #f1f5f9;
            color: var(--accent-blue);
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 10px;
            font-weight: 700;
            font-size: 12px;
        }

        .type-badge {
            padding: 4px 12px;
            background-color: #f1f5f9;
            color: var(--text-main);
            border-radius: 8px;
            font-size: 11px;
            font-weight: 700;
            text-transform: uppercase;
        }

        .money-val { font-family: 'Monaco', monospace; font-weight: 600; font-size: 14px; }
        .text-due { color: var(--danger-red); }

        .btn-op { border-radius: 8px; font-weight: 700; font-size: 13px; }

        #toast {
            visibility: hidden; min-width: 280px; background-color: var(--primary-navy); color: #fff;
            text-align: center; border-radius: 12px; padding: 16px; position: fixed;
            z-index: 2000; right: 20px; top: 20px; font-size: 14px; font-weight: 600;
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
            Management / Customer Directory
        </div>
    </header>

    <main class="main-content">
        <div class="data-card">
            <div class="card-header-custom">
                <div>
                    <h4 class="m-0" style="font-weight: 800; color: var(--primary-navy);">Customer Directory</h4>
                    <p class="m-0 text-muted" style="font-size: 13px;">Overview of client balances and contact information.</p>
                </div>
                <a href="${pageContext.request.contextPath}/customer/add" class="btn btn-primary"
                   style="border-radius: 10px; font-weight: 700; padding: 10px 24px; background-color: var(--accent-blue); border: none;">
                    + New Customer
                </a>
            </div>

            <div class="table-responsive">
                <table class="table table-hover align-middle m-0">
                    <thead>
                        <tr>
                            <th class="ps-4">SL.</th>
                            <th>Image</th>
                            <th>Customer Name</th>
                            <th>Type</th>
                            <th>Phone</th>
                            <th>Due Balance</th>
                            <th>Credit Limit</th>
                            <th class="text-end pe-4">Operations</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty customers}">
                                <c:forEach var="customer" items="${customers}" varStatus="status">
                                    <tr>
                                        <td class="ps-4" style="font-weight: 600; font-size: 13px;">
                                            ${status.count}
                                        </td>
                                        <td>
                                            <div class="customer-avatar">
                                                <c:choose>
                                                    <c:when test="${fn:length(customer.name) >= 2}">
                                                        ${fn:toUpperCase(fn:substring(customer.name, 0, 2))}
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${fn:toUpperCase(customer.name)}
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </td>
                                        <td>
                                            <div style="font-weight: 800; color: var(--primary-navy);">${customer.name}</div>
                                            <div style="font-size: 12px; color: var(--text-muted);">${customer.username}</div>
                                        </td>
                                        <td>
                                            <span class="type-badge">${customer.partyType}</span>
                                        </td>
                                        <td style="font-size: 13px; font-weight: 500;">${customer.phoneNo}</td>
                                        <td>
                                            <div class="money-val text-due">
                                                <fmt:formatNumber value="${customer.balance != null ? customer.balance : 0.00}" type="number" minFractionDigits="2" />
                                            </div>
                                        </td>
                                        <td>
                                            <div class="money-val" style="color: var(--text-main);">
                                                <fmt:formatNumber value="${customer.creditLimit != null ? customer.creditLimit : 0.00}" type="number" minFractionDigits="2" />
                                            </div>
                                        </td>
                                        <td class="text-end pe-4">
                                            <div class="d-flex justify-content-end gap-2">
                                                <a class="btn btn-outline-primary btn-sm px-3 btn-op"
                                                   href="${pageContext.request.contextPath}/customer/get?phoneNo=${customer.phoneNo}">Edit</a>
                                                <a class="btn btn-outline-danger btn-sm px-3 btn-op"
                                                   href="${pageContext.request.contextPath}/customer/delete?phoneNo=${customer.phoneNo}"
                                                   onclick="return confirm('Remove this customer from records?');">Delete</a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr><td colspan="8" class="text-center py-5 text-muted">No customers found in the system.</td></tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <script>
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