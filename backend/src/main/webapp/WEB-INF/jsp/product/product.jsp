<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Edit Master Record</title>
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
            padding: 40px 20px;
            display: flex;
            justify-content: center;
            width: 100%;
            box-sizing: border-box;
        }

        .form-card {
            width: 100%;
            max-width: 700px;
            background: var(--card-bg);
            padding: 40px;
            border-radius: 24px;
            border: 1px solid var(--border-color);
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.05);
        }

        .form-group { margin-bottom: 24px; }
        .form-group label {
            display: block;
            font-size: 11px;
            font-weight: 700;
            margin-bottom: 12px;
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
            background-color: #fff;
            transition: all 0.3s ease;
        }

        .input-control[readonly] {
            background-color: #f1f5f9;
            color: #94a3b8;
            cursor: not-allowed;
            border-style: dashed;
        }

        .checkbox-grid-container {
            background: #ffffff;
            border: 1.5px solid var(--border-color);
            border-radius: 12px;
            padding: 20px;
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(160px, 1fr));
            gap: 12px;
            max-height: 200px;
            overflow-y: auto;
        }

        .category-checkbox-item {
            display: flex;
            align-items: center;
            gap: 10px;
            font-size: 13px;
            font-weight: 600;
            color: var(--text-main);
            cursor: pointer;
        }

        .btn-submit {
            padding: 12px 32px;
            background-color: var(--primary-navy);
            color: white;
            border: none;
            border-radius: 12px;
            font-size: 15px;
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
        <a href="${pageContext.request.contextPath}/product/list" class="btn-back">
            <span>&larr;</span> Back
        </a>
        <div style="font-size: 11px; font-weight: 800; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.1em;">
            Catalog / Update Master Entry
        </div>
    </header>

    <main class="main-content">
        <div class="form-card">
            <div class="mb-4">
                <h2 style="font-weight: 800; color: var(--primary-navy); margin: 0;">Edit Master Entry</h2>
                <p style="color: var(--text-muted); font-size: 14px;">Modify the identity, brand, and classifications for this item.</p>
            </div>

            <form:form action="${pageContext.request.contextPath}/product/update" method="POST" modelAttribute="productDto">

                <%-- Hidden fields to ensure IDs are passed correctly --%>
                <form:hidden path="identifier" />

                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>Product Key (Fixed)</label>
                            <input type="text" class="input-control" value="${productDto.identifier}" readonly />
                        </div>
                    </div>
                </div>

                <%-- Brand and Model Section --%>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>Brand</label>
                            <form:select path="brand" class="input-control" required="true">
                                <form:option value="" label="-- Select Brand --"/>
                                <form:options items="${brands}" itemValue="identifier" itemLabel="identifier" />
                            </form:select>
                            <form:errors path="brand" class="text-danger small" />
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label>Model</label>
                            <form:select path="model" class="input-control" required="true">
                                <form:option value="" label="-- Select Model --"/>
                                <form:options items="${models}" itemValue="identifier" itemLabel="identifier" />
                            </form:select>
                            <form:errors path="model" class="text-danger small" />
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label>Categories (Multiple Selection)</label>
                            <div class="checkbox-grid-container">
                                <form:checkboxes path="categories"
                                                 items="${categories}"
                                                 itemValue="identifier"
                                                 itemLabel="identifier"
                                                 element="div"
                                                 cssClass="category-checkbox-item" />
                            </div>
                            <form:errors path="categories" class="text-danger small" />
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label>Unit of Measure</label>
                            <form:select path="unit" class="input-control" required="true">
                                <form:option value="" label="-- Select Unit --"/>
                                <c:forEach var="u" items="${units}">
                                    <form:option value="${u.identifier}" label="${u.identifier}"/>
                                </c:forEach>
                            </form:select>
                            <form:errors path="unit" class="text-danger small" />
                        </div>
                    </div>
                </div>

                <%-- Brand Description Group --%>
                <div class="form-group">
                    <label>Description</label>
                    <form:textarea path="description" class="input-control" rows="3"
                                   placeholder="Enter details about the product.." />
                    <form:errors path="description" cssClass="text-danger" style="font-size: 12px;"/>
                </div>

                <div class="d-flex justify-content-between align-items-center mt-4">
                    <a href="${pageContext.request.contextPath}/product/list" class="btn-cancel">Cancel</a>
                    <button type="submit" class="btn-submit">Update Record</button>
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
        };
    </script>
</body>
</html>