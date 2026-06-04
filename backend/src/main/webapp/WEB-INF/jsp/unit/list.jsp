<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Unit Management</title>
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
            margin: 0; font-family: 'Inter', system-ui, -apple-system, sans-serif;
            background-color: var(--bg-body); color: var(--text-main); min-height: 100vh;
        }

        .top-navbar {
            background: rgba(255, 255, 255, 0.9); backdrop-filter: blur(8px);
            height: 70px; display: flex; align-items: center;
            justify-content: space-between; padding: 0 40px;
            border-bottom: 1px solid var(--border-color); position: sticky; top: 0; z-index: 1000;
        }

        .btn-home {
            display: flex; align-items: center; gap: 8px;
            text-decoration: none; color: var(--primary-navy);
            font-weight: 700; font-size: 14px; padding: 8px 16px;
            border-radius: 8px; background: #f1f5f9; transition: all 0.2s;
        }
        .btn-home:hover { background: #e2e8f0; transform: translateY(-1px); }

        .main-content { padding: 40px; max-width: 1200px; margin: 0 auto; width: 100%; }

        .data-card {
            background: var(--card-bg); border-radius: 16px;
            border: 1px solid var(--border-color);
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
            overflow: hidden;
        }

        .card-header-custom {
            padding: 24px 32px; background: white;
            border-bottom: 1px solid var(--border-color);
            display: flex; justify-content: space-between; align-items: center;
        }

        .table thead th {
            background-color: #f8fafc; text-transform: uppercase;
            font-size: 11px; letter-spacing: 0.05em; font-weight: 700;
            color: var(--text-muted); padding: 15px 20px;
        }

        .unit-tag {
            padding: 4px 12px; background-color: #f1f5f9;
            color: var(--primary-navy); border-radius: 6px;
            font-size: 13px; font-weight: 700; border: 1px solid var(--border-color);
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

        .status-label { font-size: 11px; font-weight: 800; letter-spacing: 0.02em; }
        .text-active { color: var(--success-green); }
        .text-inactive { color: var(--danger-red); }

        .form-check-input { cursor: pointer; }
        .form-check-input:checked { background-color: var(--success-green) !important; border-color: var(--success-green) !important; }

        .btn-op { border-radius: 8px; font-weight: 700; font-size: 13px; }

        #toast {
            visibility: hidden; min-width: 280px; background-color: var(--primary-navy);
            color: #fff; text-align: center; border-radius: 12px; padding: 16px;
            position: fixed; z-index: 2000; right: 20px; top: 20px;
            font-size: 14px; font-weight: 600; border-left: 5px solid var(--success-green);
        }
        #toast.show { visibility: visible; animation: slideIn 0.5s forwards; }
        @keyframes slideIn { from { transform: translateX(120%); } to { transform: translateX(0); } }
    </style>
</head>
<body>

    <div id="toast">Status Updated Successfully</div>

    <header class="top-navbar">
        <a href="${pageContext.request.contextPath}/" class="btn-home">
            <span>&larr;</span> Back to Home
        </a>
        <div style="font-size: 12px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.1em;">
            Inventory / Unit Directory
        </div>
    </header>

    <main class="main-content">
        <div class="data-card">
            <div class="card-header-custom">
                <div>
                    <h4 class="m-0" style="font-weight: 800; color: var(--primary-navy);">Unit of Measure</h4>
                    <p class="m-0 text-muted" style="font-size: 13px;">Manage measurement types and visibility status.</p>
                </div>
                <a href="${pageContext.request.contextPath}/unit/add" class="btn btn-primary"
                   style="border-radius: 10px; font-weight: 700; padding: 10px 24px; background-color: var(--accent-blue); border: none;">
                    + New Unit
                </a>
            </div>

            <div class="table-responsive">
                <table class="table table-hover align-middle m-0">
                    <thead>
                        <tr>
                            <th class="ps-4">System ID</th>
                            <th>Unit Identifier</th>
                            <th>Status (Toggle)</th>
                            <th class="text-end pe-4">Operations</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${fn:length(units) > 0}">
                                <c:forEach var="u" items="${units}">
                                    <tr>
                                        <td class="ps-4">
                                            <div style="font-weight: 600; color: var(--text-muted);"># ${u.id}</div>
                                        </td>

                                        <td>
                                            <div class="d-flex align-items-center gap-2">
                                                <span class="unit-tag">
                                                    <c:out value="${u.identifier}" />
                                                </span>

                                                <%-- CLICKABLE INFO ICON (NO TITLE) --%>
                                                <i class="info-circle"
                                                   data-bs-toggle="popover"
                                                   data-bs-trigger="click"
                                                   data-bs-placement="right"
                                                   data-bs-content="${not empty u.description ? u.description : 'No description provided.'}">i</i>
                                            </div>
                                        </td>

                                        <td>
                                            <div class="form-check form-switch d-flex align-items-center">
                                                <input class="form-check-input status-toggle" type="checkbox" role="switch"
                                                       data-id="${u.identifier}"
                                                       ${u.status ? 'checked' : ''}
                                                       style="width: 34px; height: 17px;">
                                                <label class="ms-2 status-label ${u.status ? 'text-active' : 'text-inactive'}">
                                                    ${u.status ? 'ACTIVE' : 'INACTIVE'}
                                                </label>
                                            </div>
                                        </td>

                                        <td class="text-end pe-4">
                                            <div class="d-flex justify-content-end gap-2">
                                                <a class="btn btn-outline-primary btn-sm px-3 btn-op"
                                                   href="${pageContext.request.contextPath}/unit/get?identifier=${u.identifier}">Edit</a>
                                                <a class="btn btn-outline-danger btn-sm px-3 btn-op"
                                                   href="${pageContext.request.contextPath}/unit/delete?identifier=${u.identifier}"
                                                   onclick="return confirm('Remove this unit?');">Delete</a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr><td colspan="4" class="text-center py-5 text-muted">No units defined yet.</td></tr>
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

        // AJAX Toggle Logic for Units
        document.querySelectorAll('.status-toggle').forEach(toggle => {
            toggle.addEventListener('change', function() {
                const identifier = this.getAttribute('data-id');
                const isChecked = this.checked;
                const label = this.parentElement.querySelector('.status-label');
                const toast = document.getElementById("toast");

                label.innerText = isChecked ? 'ACTIVE' : 'INACTIVE';
                label.className = `ms-2 status-label ${isChecked ? 'text-active' : 'text-inactive'}`;

                fetch(`${pageContext.request.contextPath}/unit/toggle?identifier=` + identifier + `&status=` + isChecked, {
                    method: 'POST'
                })
                .then(response => {
                    if (response.ok) {
                        toast.innerText = "Unit Status Updated Successfully";
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
                    console.error("Error:", err);
                    alert("Network error. Status not updated.");
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