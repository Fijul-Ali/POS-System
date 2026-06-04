<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Master Catalog</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        :root {
            --primary-navy: #1e293b;
            --accent-blue: #2563eb;
            --success-green: #10b981;
            --danger-red: #ef4444;
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
            padding: 0 5%;
            border-bottom: 1px solid var(--border-color);
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .btn-home {
            text-decoration: none;
            color: var(--primary-navy);
            font-weight: 700;
            font-size: 14px;
            padding: 8px 16px;
            border-radius: 8px;
            background: #f1f5f9;
            transition: all 0.2s;
        }

        .main-content {
            padding: 40px 5%;
            max-width: 1400px;
            margin: 0 auto;
        }

        .data-card {
            background: var(--card-bg);
            border-radius: 20px;
            border: 1px solid var(--border-color);
            box-shadow: 0 4px 20px -2px rgba(0, 0, 0, 0.05);
            overflow: hidden;
        }

        .card-header-custom {
            padding: 30px;
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
            letter-spacing: 0.1em;
            font-weight: 800;
            color: var(--text-muted);
            padding: 18px 24px;
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

        .category-container {
            display: flex;
            flex-wrap: wrap;
            gap: 4px;
        }

        .category-pill {
            background: #eff6ff;
            color: var(--accent-blue);
            padding: 2px 10px;
            border-radius: 20px;
            font-size: 10px;
            font-weight: 800;
            text-transform: uppercase;
            border: 1px solid rgba(37, 99, 235, 0.1);
        }

        .info-label {
            font-size: 13px;
            font-weight: 700;
            color: var(--text-main);
        }

        .unit-badge {
            font-size: 12px;
            font-weight: 700;
            color: #be185d;
            background: #fdf2f8;
            padding: 4px 10px;
            border-radius: 8px;
        }

        .status-label { font-size: 11px; font-weight: 800; letter-spacing: 0.02em; }
        .text-active { color: var(--success-green); }
        .text-inactive { color: var(--danger-red); }
        .form-check-input:checked { background-color: var(--success-green) !important; border-color: var(--success-green) !important; }

        .btn-op {
            border-radius: 8px;
            font-weight: 700;
            font-size: 13px;
            padding: 6px 16px;
        }

        #toast {
            visibility: hidden; min-width: 280px; background-color: var(--primary-navy); color: #fff;
            text-align: center; border-radius: 12px; padding: 16px; position: fixed;
            z-index: 2000; right: 20px; top: 20px; font-weight: 600;
            border-left: 5px solid var(--success-green);
        }
        #toast.show { visibility: visible; animation: slideIn 0.5s forwards; }
        @keyframes slideIn { from { transform: translateX(120%); } to { transform: translateX(0); } }
    </style>
</head>
<body>

    <div id="toast">Product Status Updated Successfully</div>

    <header class="top-navbar">
        <a href="${pageContext.request.contextPath}/" class="btn-home">&larr; Home</a>
        <div style="font-size: 11px; font-weight: 800; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.1em;">
            Product Master Data
        </div>
    </header>

    <main class="main-content">
        <div class="data-card">
            <div class="card-header-custom">
                <div>
                    <h3 class="m-0" style="font-weight: 800; color: var(--primary-navy);">Master Catalog</h3>
                    <p class="m-0 text-muted" style="font-size: 14px;">Manage core product identities, brands, and inventory units.</p>
                </div>
                <a href="${pageContext.request.contextPath}/product/add" class="btn btn-primary"
                   style="border-radius: 10px; font-weight: 700; padding: 12px 24px; background-color: var(--accent-blue); border: none;">
                    + New Entry
                </a>
            </div>

            <div class="table-responsive">
                <table class="table table-hover align-middle m-0">
                    <thead>
                        <tr>
                            <th class="ps-4">Product & SKU</th>
                            <th>Brand</th>
                            <th>Model</th>
                            <th>Classifications</th>
                            <th>Unit</th>
                            <th>Status</th>
                            <th class="text-end pe-4">Manage</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${fn:length(products) > 0}">
                                <c:forEach var="product" items="${products}">
                                    <tr>
                                        <td class="ps-4 py-3">
                                            <div class="d-flex align-items-center gap-2">
                                                <div style="font-weight: 700; color: var(--primary-navy); font-size: 14px;">${product.identifier}</div>

                                                <%-- CLICKABLE INFO ICON (NO TITLE) --%>
                                                <i class="info-circle"
                                                   data-bs-toggle="popover"
                                                   data-bs-trigger="click"
                                                   data-bs-placement="right"
                                                   data-bs-content="${not empty product.description ? product.description : 'No description provided.'}">i</i>
                                            </div>
                                        </td>

                                        <td>
                                            <div class="info-label">${product.brand}</div>
                                        </td>

                                        <td>
                                            <div class="info-label">${product.model}</div>
                                        </td>

                                        <td>
                                            <div class="category-container">
                                                <c:forEach var="cat" items="${product.categories}">
                                                    <span class="category-pill">${cat}</span>
                                                </c:forEach>
                                                <c:if test="${empty product.categories}">
                                                    <span class="text-muted small italic">Unclassified</span>
                                                </c:if>
                                            </div>
                                        </td>
                                        <td>
                                            <span class="unit-badge">${product.unit}</span>
                                        </td>
                                        <td>
                                            <div class="form-check form-switch d-flex align-items-center">
                                                <input class="form-check-input status-toggle" type="checkbox" role="switch"
                                                       data-id="${product.identifier}"
                                                       ${product.status ? 'checked' : ''}
                                                       style="width: 32px; height: 16px;">
                                                <label class="ms-2 status-label ${product.status ? 'text-active' : 'text-inactive'}">
                                                    ${product.status ? 'ACTIVE' : 'INACTIVE'}
                                                </label>
                                            </div>
                                        </td>
                                        <td class="text-end pe-4">
                                            <div class="d-flex justify-content-end gap-2">
                                                <a class="btn btn-outline-primary btn-sm btn-op"
                                                   href="${pageContext.request.contextPath}/product/get?identifier=${product.identifier}">Modify</a>
                                                <a class="btn btn-outline-danger btn-sm btn-op"
                                                   href="${pageContext.request.contextPath}/product/delete?identifier=${product.identifier}"
                                                   onclick="return confirm('Remove this product from master?');">Delete</a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr><td colspan="7" class="text-center py-5 text-muted">No master records found.</td></tr>
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

        // AJAX Toggle Logic
        document.querySelectorAll('.status-toggle').forEach(toggle => {
            toggle.addEventListener('change', function() {
                const identifier = this.getAttribute('data-id');
                const isChecked = this.checked;
                const label = this.parentElement.querySelector('.status-label');
                const toast = document.getElementById("toast");

                label.innerText = isChecked ? 'ACTIVE' : 'INACTIVE';
                label.className = `ms-2 status-label ${isChecked ? 'text-active' : 'text-inactive'}`;

                fetch(`${pageContext.request.contextPath}/product/toggle?identifier=` + identifier + `&status=` + isChecked, {
                    method: 'POST'
                })
                .then(response => {
                    if (response.ok) {
                        toast.innerText = "Product Status Updated Successfully";
                        toast.className = "show";
                        setTimeout(() => { toast.className = ""; }, 3000);
                    } else {
                        this.checked = !isChecked;
                        label.innerText = !isChecked ? 'ACTIVE' : 'INACTIVE';
                        label.className = `ms-2 status-label ${!isChecked ? 'text-active' : 'text-inactive'}`;
                        alert("Error: Database sync failed.");
                    }
                })
                .catch(err => {
                    console.error("Network Error:", err);
                    alert("Network error. Could not sync status.");
                });
            });
        });

        window.onload = function() {
            const msg = "${message}";
            if (msg && msg.trim() !== "") {
                const toast = document.getElementById("toast");
                toast.innerText = msg;
                toast.className = "show";
                setTimeout(() => { toast.className = ""; }, 4000);
            }
        };
    </script>
</body>
</html>