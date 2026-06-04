<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Management Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        :root {
            --primary-navy: #0f172a;
            --accent-blue: #3b82f6;
            --sidebar-bg: #1e293b;
            --text-gray: #94a3b8;
            --border-subtle: rgba(255, 255, 255, 0.1);
            --sidebar-width: 260px;
            --card-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
        }

        body { margin: 0; font-family: 'Inter', sans-serif; background-color: #f8fafc; display: flex; }

        /* --- Sidebar --- */
        .sidebar { width: var(--sidebar-width); background-color: var(--sidebar-bg); color: #f1f5f9; display: flex; flex-direction: column; position: fixed; height: 100vh; z-index: 1000; }
        .sidebar-header { padding: 24px; font-size: 1.25rem; font-weight: 800; border-bottom: 1px solid var(--border-subtle); color: white; letter-spacing: 0.05em; }
        .nav-menu { flex-grow: 1; padding: 20px 0; overflow-y: auto; }
        .nav-item { display: flex; align-items: center; padding: 12px 24px; color: #cbd5e1; text-decoration: none; font-size: 14px; font-weight: 500; transition: 0.2s; }
        .nav-item:hover { background: rgba(255, 255, 255, 0.05); color: white; padding-left: 28px; }
        .nav-item.active { background: var(--accent-blue); color: white; }

        .sidebar-footer { padding: 20px; border-top: 1px solid var(--border-subtle); background: rgba(0, 0, 0, 0.2); }
        .user-profile-box { display: flex; align-items: center; gap: 12px; margin-bottom: 15px; }
        .avatar-circle { width: 38px; height: 38px; background: var(--accent-blue); color: white; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-weight: 700; }
        .btn-logout { width: 100%; background: transparent; color: #fca5a5; border: 1px solid rgba(252, 165, 165, 0.2); padding: 8px; border-radius: 8px; font-size: 12px; font-weight: 700; cursor: pointer; transition: 0.2s; }
        .btn-logout:hover { background: #ef4444; color: white; border-color: #ef4444; }

        /* --- Main Content --- */
        .content-wrapper { margin-left: var(--sidebar-width); flex-grow: 1; width: calc(100% - var(--sidebar-width)); }
        .hero-section { background: white; padding: 40px 60px; border-bottom: 1px solid #e2e8f0; margin-bottom: 30px; }

        /* --- Dashboard Grid & Cards --- */
        .dashboard-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 24px; padding: 0 60px 40px 60px; }

        .card-link { text-decoration: none !important; color: inherit !important; display: block; }

        .stat-card {
            background: white; padding: 28px; border-radius: 20px; border: 1px solid #e2e8f0;
            box-shadow: var(--card-shadow); transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            position: relative; overflow: hidden;
        }
        .stat-card:hover { transform: translateY(-8px); border-color: var(--accent-blue); box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1); }

        .stat-icon { width: 52px; height: 52px; border-radius: 14px; display: flex; align-items: center; justify-content: center; font-size: 24px; margin-bottom: 20px; }
        .stat-label { font-size: 12px; font-weight: 800; color: #64748b; text-transform: uppercase; letter-spacing: 0.05em; }
        .stat-value { font-size: 32px; font-weight: 800; color: var(--primary-navy); margin: 4px 0; }

        .bg-light-blue { background: #eff6ff; color: #2563eb; }
        .bg-light-green { background: #ecfdf5; color: #10b981; }
        .bg-light-purple { background: #f5f3ff; color: #7c3aed; }
        .bg-light-orange { background: #fff7ed; color: #ea580c; }

        /* --- Launchpad Matrix --- */
        .quick-access-card { background: white; padding: 40px; border-radius: 24px; border: 1px solid #e2e8f0; box-shadow: var(--card-shadow); margin: 0 60px 60px 60px; }
        .matrix-btn {
            display: block; text-decoration: none; padding: 22px; background: #f8fafc; border: 1.5px solid #e2e8f0;
            border-radius: 18px; transition: all 0.2s; height: 100%;
        }
        .matrix-btn:hover { background: white; border-color: var(--accent-blue); transform: scale(1.03); box-shadow: 0 10px 15px -3px rgba(0,0,0,0.05); }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="sidebar-header">RETAIL CORE</div>
        <nav class="nav-menu">
            <a href="${pageContext.request.contextPath}/" class="nav-item active">Dashboard Home</a>
            <c:forEach var="node" items="${nodes}">
                <c:if test="${node.identifier != 'Home'}">
                    <a href="${node.path}" class="nav-item">${node.identifier}</a>
                </c:if>
            </c:forEach>
        </nav>
        <div class="sidebar-footer">
            <div class="user-profile-box">
                <div class="avatar-circle" id="userInitial"><sec:authentication property="principal.username" /></div>
                <div class="user-info">
                    <span style="display:block; font-size: 10px; color: var(--text-gray); text-transform: uppercase;">LOGGED IN AS:</span>
                    <span style="display:block; font-size: 13px; font-weight: 700; color: white;"><sec:authentication property="principal.username" /></span>
                </div>
            </div>
            <form action="${pageContext.request.contextPath}/logout" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button type="submit" class="btn-logout">Sign Out</button>
            </form>
        </div>
    </aside>

    <div class="content-wrapper">
        <header class="hero-section">
            <div class="container-fluid p-0">
                <h1 style="font-weight: 800; color: var(--primary-navy); font-size: 2.25rem; letter-spacing: -0.02em;">Control Center</h1>
                <p style="color: #64748b; font-size: 16px; margin: 4px 0 0 0;">Managing infrastructure for <strong><sec:authentication property="principal.username" /></strong></p>
            </div>
        </header>

        <main class="dashboard-grid">
            <a href="${pageContext.request.contextPath}/brand/list" class="card-link">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-start">
                        <div class="stat-icon bg-light-blue">🏷️</div>
                        <span class="badge rounded-pill text-bg-success" style="font-size: 10px;">Verified</span>
                    </div>
                    <div class="stat-label">Total Brands</div>
                    <div class="stat-value">${brandCount}</div>
                    <div style="font-size: 12px; color: #64748b;">View all partners &rarr;</div>
                </div>
            </a>

            <a href="${pageContext.request.contextPath}/warehouse/list" class="card-link">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-start">
                        <div class="stat-icon bg-light-green">🏢</div>
                        <span class="badge rounded-pill text-bg-primary" style="font-size: 10px;">Live</span>
                    </div>
                    <div class="stat-label">Warehouse Hubs</div>
                    <div class="stat-value">${warehouseCount}</div>
                    <div style="font-size: 12px; color: #64748b;">Manage locations &rarr;</div>
                </div>
            </a>

            <a href="${pageContext.request.contextPath}/shelfs/list" class="card-link">
                <div class="stat-card">
                    <div class="d-flex justify-content-between align-items-start">
                        <div class="stat-icon bg-light-purple">🏗️</div>
                        <span class="badge rounded-pill text-bg-info text-white" style="font-size: 10px;">Allocated</span>
                    </div>
                    <div class="stat-label">Shelf Units</div>
                    <div class="stat-value">${shelfCount}</div>
                    <div style="font-size: 12px; color: #64748b;">Audit capacity &rarr;</div>
                </div>
            </a>

            <div class="stat-card">
                <div class="d-flex justify-content-between align-items-start">
                    <div class="stat-icon bg-light-orange">⚙️</div>
                    <span class="badge rounded-pill text-bg-secondary" style="font-size: 10px;">Core</span>
                </div>
                <div class="stat-label">Active Modules</div>
                <div class="stat-value">${moduleCount}</div>
                <div style="font-size: 12px; color: #64748b;">Permissions-based access</div>
            </div>
        </main>

        <div class="quick-access-card">
            <div class="mb-4">
                <h5 style="font-weight: 800; color: var(--primary-navy); margin-bottom: 8px;">Quick Launchpad</h5>
                <p style="color: #64748b; font-size: 14px;">Specific operational tasks and entry points.</p>
            </div>

            <div class="row g-4">
                <div class="col-md-3">
                    <a href="${pageContext.request.contextPath}/stock/list" class="matrix-btn">
                        <span style="display:block; font-size: 11px; font-weight: 800; color: var(--accent-blue); text-transform: uppercase; margin-bottom: 4px;">Inventory Control</span>
                        <span style="font-weight: 700; color: var(--primary-navy);">Stock Ledger</span>
                    </a>
                </div>
                <div class="col-md-3">
                    <a href="${pageContext.request.contextPath}/racks/list" class="matrix-btn">
                        <span style="display:block; font-size: 11px; font-weight: 800; color: var(--accent-blue); text-transform: uppercase; margin-bottom: 4px;">Organization</span>
                        <span style="font-weight: 700; color: var(--primary-navy);">Manage Racks</span>
                    </a>
                </div>
                <div class="col-md-3">
                    <a href="${pageContext.request.contextPath}/unit/list" class="matrix-btn">
                        <span style="display:block; font-size: 11px; font-weight: 800; color: var(--accent-blue); text-transform: uppercase; margin-bottom: 4px;">Definitions</span>
                        <span style="font-weight: 700; color: var(--primary-navy);">Measurement Units</span>
                    </a>
                </div>
                <div class="col-md-3">
                    <a href="${pageContext.request.contextPath}/brand/add" class="matrix-btn" style="background: #eff6ff; border-color: #dbeafe;">
                        <span style="display:block; font-size: 11px; font-weight: 800; color: var(--accent-blue); text-transform: uppercase; margin-bottom: 4px;">Registration</span>
                        <span style="font-weight: 700; color: var(--accent-blue);">+ Add New Brand</span>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script>
        window.onload = function() {
            const badge = document.getElementById('userInitial');
            if (badge) {
                const text = badge.innerText.trim();
                badge.innerText = text.charAt(0);
            }
        };
    </script>
</body>
</html>