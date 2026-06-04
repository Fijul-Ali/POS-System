<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Edit Racks</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        :root {
            --primary-navy: #1e293b;
            --accent-blue: #2563eb;
            --success-green: #10b981;
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

        .btn-back {
            display: flex; align-items: center; gap: 8px;
            text-decoration: none; color: var(--primary-navy);
            font-weight: 700; font-size: 14px; padding: 8px 16px;
            border-radius: 8px; background: #f1f5f9; transition: all 0.2s;
        }
        .btn-back:hover { background: #e2e8f0; transform: translateY(-1px); }

        .main-content { padding: 60px 40px; display: flex; justify-content: center; width: 100%; }

        .form-card {
            width: 100%; max-width: 520px; background: var(--card-bg);
            padding: 40px; border-radius: 20px; border: 1px solid var(--border-color);
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.05);
        }

        .form-group { margin-bottom: 24px; }
        .form-group label {
            display: block; font-size: 11px; font-weight: 700;
            margin-bottom: 8px; color: var(--text-muted);
            text-transform: uppercase; letter-spacing: 0.05em;
        }

        .input-control {
            width: 100%; padding: 12px 16px; border: 1.5px solid var(--border-color);
            border-radius: 12px; font-size: 14px; transition: all 0.3s ease;
            background-color: white;
        }
        .input-control:focus { outline: none; border-color: var(--accent-blue); box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.1); }

        /* Multi-select styling */
        select.input-control[multiple] { height: auto; min-height: 100px; padding: 8px; }

        .input-readonly { background-color: #f1f5f9; cursor: not-allowed; color: var(--text-muted); font-weight: 600; }

        /* --- INFO CIRCLE STYLES (Consistency with List Pages) --- */
        .info-circle {
            display: inline-flex; align-items: center; justify-content: center;
            width: 20px; height: 20px; background: #eff6ff; color: var(--accent-blue);
            border-radius: 50%; font-size: 11px; font-weight: 800;
            cursor: pointer; transition: all 0.2s; border: 1.5px solid #dbeafe;
            font-style: normal;
        }
        .info-circle:hover { background: var(--accent-blue); color: white; }

        .status-container {
            display: flex; align-items: center; justify-content: space-between;
            background: #f8fafc; padding: 16px; border-radius: 12px;
            border: 1px solid var(--border-color);
        }

        .btn-submit {
            width: 100%; padding: 14px; background-color: var(--primary-navy);
            color: white; border: none; border-radius: 12px;
            font-size: 15px; font-weight: 700; cursor: pointer; transition: all 0.2s;
        }
        .btn-submit:hover { background-color: #0f172a; transform: translateY(-2px); }

        .form-check-input:checked { background-color: var(--success-green); border-color: var(--success-green); }

        .popover { border-radius: 10px; border: 1px solid var(--border-color); box-shadow: 0 10px 15px -3px rgba(0,0,0,0.1); padding: 5px; }
    </style>
</head>
<body>

    <header class="top-navbar">
        <a href="${pageContext.request.contextPath}/racks/list" class="btn-back">
            <span>&larr;</span> Cancel Changes
        </a>
        <div style="font-size: 12px; font-weight: 700; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.1em;">
            Inventory / Modify Racks
        </div>
    </header>

    <main class="main-content">
        <div class="form-card">
            <div class="mb-4">
                <h2 style="font-weight: 800; color: var(--primary-navy); margin: 0;">Edit Racks</h2>
                <div class="d-flex align-items-center gap-2 mt-1">
                    <p class="m-0 text-muted" style="font-size: 14px;">Updating record: <strong class="text-primary">${racksDto.identifier}</strong></p>
                    <%-- Info Icon for current record description --%>
                    <i class="info-circle" data-bs-toggle="popover" data-bs-trigger="click" data-bs-placement="right"
                       data-bs-content="${not empty racksDto.description ? racksDto.description : 'No description set.'}">i</i>
                </div>
            </div>

            <form:form id="editRacksForm" method="POST" action="update" modelAttribute="racksDto">
                <form:hidden path="id" />

                <div class="form-group">
                    <label>Racks Identifier (Read-Only)</label>
                    <form:input path="identifier" class="input-control input-readonly" readonly="true"/>
                </div>

                <div class="form-group">
                    <label>Assigned Shelves</label>
                    <form:select path="shelves" class="input-control" required="true" multiple="true">
                        <c:forEach var="shelf" items="${shelfs}">
                            <form:option value="${shelf.identifier}" label="${shelf.identifier}"/>
                        </c:forEach>
                    </form:select>
                    <div class="mt-2" style="font-size: 11px; color: var(--text-muted);">
                        <i class="bi bi-info-circle"></i> Hold <strong>Ctrl (Win)</strong> or <strong>Cmd (Mac)</strong> to select multiple.
                    </div>
                </div>

                <div class="form-group">
                    <label>Rack Description</label>
                    <form:textarea path="description" class="input-control" rows="3" placeholder="Describe the purpose of this rack..."/>
                </div>

                <div class="form-group">
                    <label>Availability Status</label>
                    <div class="status-container">
                        <span style="font-size: 13px; font-weight: 600; color: var(--primary-navy);">Rack Active Status</span>
                        <div class="form-check form-switch m-0">
                            <form:checkbox path="status" class="form-check-input" role="switch" id="statusToggle" style="width: 40px; height: 20px;"/>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn-submit">Update Racks</button>
            </form:form>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Initialize Popover
        document.addEventListener('DOMContentLoaded', function () {
            var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
            var popoverList = popoverTriggerList.map(function (el) {
                return new bootstrap.Popover(el)
            });

            // Close popover when clicking elsewhere
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