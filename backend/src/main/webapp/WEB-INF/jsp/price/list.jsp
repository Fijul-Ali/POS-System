<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Pricing Master</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        :root {
            --primary-navy: #0f172a;
            --accent-blue: #3b82f6;
            --success-green: #10b981;
            --danger-red: #ef4444;
            --bg-body: #f1f5f9;
            --card-bg: #ffffff;
            --text-main: #1e293b;
            --border-color: #e2e8f0;
        }

        body {
            margin: 0;
            font-family: 'Inter', sans-serif;
            background-color: var(--bg-body);
            color: var(--text-main);
        }

        .top-navbar {
            background: white;
            height: 70px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 5%;
            border-bottom: 1px solid var(--border-color);
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .main-content { padding: 30px 5%; margin: 0 auto; max-width: 1400px; }

        .data-card {
            background: var(--card-bg);
            border-radius: 16px;
            border: 1px solid var(--border-color);
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
            overflow: hidden;
        }

        .card-header-custom {
            padding: 24px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid var(--border-color);
        }

        .table thead th {
            background-color: #f8fafc;
            text-transform: uppercase;
            font-size: 11px;
            letter-spacing: 0.05em;
            font-weight: 700;
            color: #64748b;
            padding: 15px 20px;
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

        .price-val { font-weight: 700; color: var(--primary-navy); }
        .mrp-val { font-size: 12px; text-decoration: line-through; color: #94a3b8; }

        .margin-val {
            font-size: 13px;
            font-weight: 600;
            color: var(--success-green);
        }

        .btn-op { border-radius: 8px; font-weight: 700; font-size: 12px; }
    </style>
</head>
<body>

    <header class="top-navbar">
        <a href="${pageContext.request.contextPath}/" class="btn btn-light btn-sm fw-bold">← Home</a>
        <div class="fw-bold text-uppercase" style="font-size: 12px; letter-spacing: 1px; color: #64748b;">Financial / Price Master</div>
    </header>

    <main class="main-content">
        <div class="data-card">
            <div class="card-header-custom">
                <div>
                    <h4 class="m-0 fw-bold" style="color: var(--primary-navy);">Pricing Catalog</h4>
                    <p class="m-0 text-muted" style="font-size: 13px;">Manage product cost, margins, and MRP validation.</p>
                </div>
                <a href="${pageContext.request.contextPath}/price/add" class="btn btn-primary fw-bold" style="border-radius: 10px; background-color: var(--accent-blue); border: none;">
                    + Set New Price
                </a>
            </div>

            <div class="table-responsive">
                <table class="table table-hover align-middle m-0">
                    <thead>
                        <tr>
                            <th class="ps-4">Product ID</th>
                            <th>Cost Price</th>
                            <th>Selling Price / MRP</th>
                            <th>Profit Margin</th>
                            <th>Validity Period</th>
                            <th class="text-end pe-4">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${fn:length(prices) > 0}">
                                <c:forEach var="price" items="${prices}">
                                    <tr>
                                        <td class="ps-4">
                                            <div class="d-flex align-items-center gap-2">
                                                <span class="fw-bold" style="color: var(--accent-blue);">${price.identifier}</span>

                                                <%-- CLICKABLE INFO ICON (NO TITLE) --%>
                                                <i class="info-circle"
                                                   data-bs-toggle="popover"
                                                   data-bs-trigger="click"
                                                   data-bs-placement="right"
                                                   data-bs-content="${not empty price.description ? price.description : 'No description provided.'}">i</i>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="price-val">₹<fmt:formatNumber value="${price.costPrice}" pattern="#,##0.00"/></div>
                                        </td>
                                        <td>
                                            <div class="price-val">₹<fmt:formatNumber value="${price.sellingPrice}" pattern="#,##0.00"/></div>
                                            <div class="mrp-val">MRP: ₹<fmt:formatNumber value="${price.mrp}" pattern="#,##0.00"/></div>
                                        </td>
                                        <td>
                                            <div class="margin-val">
                                                <c:set var="profit" value="${price.sellingPrice - price.costPrice}" />
                                                +₹<fmt:formatNumber value="${profit}" pattern="#,##0.00"/>
                                            </div>
                                        </td>
                                        <td>
                                            <div style="font-size: 12px; font-weight: 500;">
                                                <fmt:parseDate value="${price.effectiveFrom}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedFrom" type="both" />
                                                <fmt:formatDate value="${parsedFrom}" pattern="dd MMM yyyy" />
                                                <span class="text-muted mx-1">to</span>
                                                <fmt:parseDate value="${price.effectiveTo}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedTo" type="both" />
                                                <fmt:formatDate value="${parsedTo}" pattern="dd MMM yyyy" />
                                            </div>
                                        </td>
                                        <td class="text-end pe-4">
                                            <div class="d-flex justify-content-end gap-2">
                                                <a class="btn btn-outline-primary btn-sm btn-op"
                                                   href="${pageContext.request.contextPath}/price/get?identifier=${price.identifier}">Edit</a>
                                                <a class="btn btn-outline-danger btn-sm btn-op"
                                                   href="${pageContext.request.contextPath}/price/delete?identifier=${price.identifier}"
                                                   onclick="return confirm('Archive this price record?');">Delete</a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr><td colspan="6" class="text-center py-5 text-muted">No price records found.</td></tr>
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
    </script>
</body>
</html>