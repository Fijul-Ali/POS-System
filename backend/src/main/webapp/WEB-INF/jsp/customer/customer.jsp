<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Retail POS | Update Customer</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <style>
        :root {
            --primary-navy: #1e293b; --accent-blue: #2563eb; --bg-body: #f8fafc;
            --card-bg: #ffffff; --text-main: #1e293b; --text-muted: #64748b;
            --border-color: #e2e8f0; --warning-orange: #f59e0b;
        }
        body { margin: 0; font-family: 'Inter', sans-serif; background-color: var(--bg-body); color: var(--text-main); min-height: 100vh; }
        .top-navbar { background: white; height: 70px; display: flex; align-items: center; padding: 0 40px; border-bottom: 1px solid var(--border-color); position: sticky; top: 0; z-index: 1000; }
        .btn-back { text-decoration: none; color: var(--primary-navy); font-weight: 700; background: #f1f5f9; padding: 8px 16px; border-radius: 8px; font-size: 14px; }
        .main-content { padding: 40px; max-width: 1100px; margin: 0 auto; width: 100%; }
        .data-card { background: white; border-radius: 16px; border: 1px solid var(--border-color); box-shadow: 0 4px 6px rgba(0,0,0,0.05); }
        .card-header-custom { padding: 24px 32px; border-bottom: 1px solid var(--border-color); border-left: 5px solid var(--warning-orange); }
        .form-section-title { font-size: 11px; font-weight: 800; text-transform: uppercase; color: var(--accent-blue); margin-bottom: 20px; display: flex; align-items: center; gap: 8px; }
        .form-label { font-size: 13px; font-weight: 700; margin-bottom: 8px; display: block; }
        .form-control, .form-select { border-radius: 10px; padding: 10px 14px; border: 1px solid var(--border-color); font-size: 14px; }
        .address-group { background-color: #f8fafc; padding: 24px; border-radius: 12px; border: 1px solid var(--border-color); margin-bottom: 24px; }
        .btn-update { background-color: var(--warning-orange); border: none; border-radius: 10px; color: white; font-weight: 700; padding: 12px 32px; transition: 0.2s; }
        .btn-update:hover { background-color: #d97706; }
    </style>
</head>
<body>

    <header class="top-navbar">
        <a href="${pageContext.request.contextPath}/customer/list" class="btn-back">&larr; Cancel Update</a>
    </header>

    <main class="main-content">
        <div class="data-card">
            <div class="card-header-custom">
                <h4 class="m-0" style="font-weight: 800;">Update Customer Profile</h4>
                <p class="text-muted m-0" style="font-size: 13px;">Modify details for: <strong>${customerDto.name}</strong></p>
            </div>

            <div class="card-body p-4">
                <form:form action="${pageContext.request.contextPath}/customer/update" method="post" modelAttribute="customerDto">

                    <%-- HIDDEN IDs: Crucial for updating existing records without creating duplicates --%>
                    <form:hidden path="id" />
                    <form:hidden path="billingAddress.id" />
                    <form:hidden path="shippingAddress.id" />

                    <div class="form-section-title"><i class="bi bi-person-gear"></i> Account Settings</div>
                    <div class="row g-3 mb-4">
                        <div class="col-md-6">
                            <label class="form-label">Full Name</label>
                            <form:input path="name" class="form-control" required="required"/>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Email / Username</label>
                            <form:input path="username" class="form-control" readonly="true" style="background-color: #e9ecef;"/>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Phone Number</label>
                            <form:input path="phoneNo" class="form-control"/>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Party Type</label>
                            <form:select path="partyType" class="form-select">
                                <form:option value="Customer">Customer</form:option>
                                <form:option value="Dealer">Dealer</form:option>
                                <form:option value="Wholesaler">Wholesaler</form:option>
                            </form:select>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">Credit Limit</label>
                            <form:input path="creditLimit" class="form-control" />
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Current Balance</label>
                            <form:input path="balance" class="form-control" />
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Balance Status</label>
                            <form:select path="balanceType" class="form-select">
                                <form:option value="due">Due</form:option>
                                <form:option value="advance">Advance</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="form-section-title"><i class="bi bi-geo-alt"></i> Billing Address</div>
                    <div class="address-group">
                        <div class="row g-3">
                            <div class="col-12">
                                <label class="form-label">Street Address</label>
                                <form:input id="b_addr" path="billingAddress.line1" class="form-control" />
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">City</label>
                                <form:input id="b_city" path="billingAddress.city" class="form-control" />
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">State</label>
                                <form:input id="b_state" path="billingAddress.state" class="form-control" />
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">Zip Code</label>
                                <form:input id="b_zip" path="billingAddress.zipCode" class="form-control" />
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">Country</label>
                                <form:input id="b_country" path="billingAddress.country" class="form-control" />
                            </div>
                        </div>
                    </div>

                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <div class="form-section-title mb-0"><i class="bi bi-truck"></i> Shipping Address</div>
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="copyCheck" onclick="copyAddress()">
                            <label class="form-check-label fw-bold" for="copyCheck" style="font-size: 12px;">Sync with Billing</label>
                        </div>
                    </div>

                    <div class="address-group">
                        <div class="row g-3">
                            <div class="col-12">
                                <label class="form-label">Street Address</label>
                                <form:input id="s_addr" path="shippingAddress.line1" class="form-control" />
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">City</label>
                                <form:input id="s_city" path="shippingAddress.city" class="form-control" />
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">State</label>
                                <form:input id="s_state" path="shippingAddress.state" class="form-control" />
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">Zip Code</label>
                                <form:input id="s_zip" path="shippingAddress.zipCode" class="form-control" />
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">Country</label>
                                <form:input id="s_country" path="shippingAddress.country" class="form-control" />
                            </div>
                        </div>
                    </div>

                    <div class="text-end pt-3">
                        <button type="submit" class="btn btn-update">Update Customer Info</button>
                    </div>
                </form:form>
            </div>
        </div>
    </main>

    <script>
        function copyAddress() {
            var check = document.getElementById("copyCheck");
            if (check.checked) {
                document.getElementById("s_addr").value = document.getElementById("b_addr").value;
                document.getElementById("s_city").value = document.getElementById("b_city").value;
                document.getElementById("s_state").value = document.getElementById("b_state").value;
                document.getElementById("s_zip").value = document.getElementById("b_zip").value;
                document.getElementById("s_country").value = document.getElementById("b_country").value;
            }
        }
    </script>
</body>
</html>