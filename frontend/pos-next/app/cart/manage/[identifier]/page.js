"use client";
import React, { useEffect, useState } from 'react';
import { useRouter, useParams } from 'next/navigation';
import apiClient from '@/utils/apiClient';
import Layout from '@/app/components/layout';

function CartManage() {
    const router = useRouter();
    const { identifier } = useParams();

    const [cart, setCart] = useState(null);
    const [entries, setEntries] = useState([]);
    const [prices, setPrices] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const [selectedProduct, setSelectedProduct] = useState('');
    const [quantity, setQuantity] = useState(1);
    const [adding, setAdding] = useState(false);
    const [addError, setAddError] = useState('');

    const [recalculating, setRecalculating] = useState(false);
    const [checkedOut, setCheckedOut] = useState(false);
    const [checkingOut, setCheckingOut] = useState(false);

    useEffect(() => {
        if (identifier) fetchAll();
    }, [identifier]);

    const fetchAll = async () => {
        setLoading(true);
        setError('');
        try {
            await Promise.all([fetchCart(), fetchEntries(), fetchPrices()]);
        } finally {
            setLoading(false);
        }
    };

    const fetchCart = async () => {
        try {
            const response = await apiClient.post('/api/cart/getCart', { identifier });
            if (!response) return;
            setCart(response.data);
        } catch (err) {
            setError('Failed to load cart');
        }
    };

    const fetchEntries = async () => {
        try {
            const response = await apiClient.post('/api/cartEntry/list', {
                page: 0,
                sizePerPage: 100,
                sortDirection: 'ASC',
                sortField: 'id'
            });
            if (!response) return;
            const all = response.data.dtoList || [];
            setEntries(all.filter(entry => entry.cart === identifier));
        } catch (err) {
            setError('Failed to load cart entries');
        }
    };

    const fetchPrices = async () => {
        try {
            const response = await apiClient.post('/api/price/list', {
                page: 0,
                sizePerPage: 200,
                sortDirection: 'ASC',
                sortField: 'id'
            });
            if (!response) return;
            setPrices(response.data.dtoList || []);
        } catch (err) {}
    };

    const handleAddEntry = async () => {
        setAddError('');
        if (!selectedProduct) { setAddError('Please select a product'); return; }
        if (!quantity || quantity < 1) { setAddError('Quantity must be at least 1'); return; }
        setAdding(true);
        try {
            const response = await apiClient.post('/api/cartEntry/addEntry', {
                product: selectedProduct,
                quantity: Number(quantity),
                cart: identifier
            });
            if (!response) return;
            setSelectedProduct('');
            setQuantity(1);
            setSuccess('Product added. Click Recalculate to update totals.');
            await fetchEntries();
        } catch (err) {
            setAddError('Failed to add product to cart');
        } finally {
            setAdding(false);
        }
    };

    const handleRemoveEntry = async (entryIdentifier) => {
        if (!window.confirm('Remove this item from cart?')) return;
        try {
            await apiClient.post('/api/cartEntry/delete', { identifier: entryIdentifier });
            setEntries(prev => prev.filter(e => e.identifier !== entryIdentifier));
            setSuccess('Item removed. Click Recalculate to update totals.');
        } catch (err) {
            setError('Failed to remove item');
        }
    };

    const handleRecalculate = async () => {
        setRecalculating(true);
        setError('');
        setSuccess('');
        try {
            const response = await apiClient.post('/api/cart/addToCart', { cart: identifier });
            if (!response) return;
            setCart(response.data);
            setSuccess('Cart totals updated successfully.');
        } catch (err) {
            setError('Failed to recalculate cart');
        } finally {
            setRecalculating(false);
        }
    };

    const handleCheckout = async () => {
        if (!window.confirm(`Checkout cart ${identifier}? This will create a permanent order.`)) return;
        setCheckingOut(true);
        setError('');
        setSuccess('');
        try {
            const response = await apiClient.post('/api/order/place', { cart: identifier });
            if (!response) return;
            const order = response.data;
            if (order.success === false) {
                setError(order.message || 'Checkout failed');
                return;
            }
            setCheckedOut(true);
            setSuccess(`Order placed successfully! Order ID: ${order.identifier?.slice(0, 8)}...`);
        } catch (err) {
            setError('Failed to place order');
        } finally {
            setCheckingOut(false);
        }
    };

    if (loading) return (
        <Layout>
            <div className='flex justify-center items-center h-64'>
                <p className='text-gray-400 text-sm'>Loading cart...</p>
            </div>
        </Layout>
    );

    return (
        <Layout>
            <div className='p-6'>
                <div className='flex justify-between items-center mb-6'>
                    <div>
                        <button
                            onClick={() => router.push('/cart')}
                            className='text-xs text-gray-400 hover:text-gray-600 transition mb-1 flex items-center gap-1'
                        >
                            ← Back to Carts
                        </button>
                        <h1 className='text-xl font-bold text-gray-800'>Cart</h1>
                        <p className='text-sm text-gray-500 mt-0.5'>{identifier}</p>
                    </div>
                    <div className='flex gap-2'>
                        <button
                            onClick={handleRecalculate}
                            disabled={recalculating || checkedOut}
                            className='bg-green-600 hover:bg-green-700 text-white text-sm font-semibold py-2 px-4 rounded-lg transition disabled:opacity-50'
                        >
                            {recalculating ? 'Recalculating...' : '↻ Recalculate'}
                        </button>
                        <button
                            onClick={handleCheckout}
                            disabled={checkingOut || checkedOut || entries.length === 0}
                            className='bg-blue-600 hover:bg-blue-700 text-white text-sm font-semibold py-2 px-4 rounded-lg transition disabled:opacity-50'
                        >
                            {checkingOut ? 'Processing...' : checkedOut ? '✓ Checked Out' : 'Checkout'}
                        </button>
                    </div>
                </div>

                {error && (
                    <div className='bg-red-50 text-red-600 text-sm px-4 py-2 rounded-lg mb-4 border border-red-200'>
                        {error}
                    </div>
                )}
                {success && (
                    <div className='bg-green-50 text-green-700 text-sm px-4 py-2 rounded-lg mb-4 border border-green-200'>
                        {success}
                        {checkedOut && (
                            <button
                                onClick={() => router.push('/order')}
                                className='ml-3 underline font-semibold'
                            >
                                View Orders →
                            </button>
                        )}
                    </div>
                )}

                {cart && (
                    <div className='grid grid-cols-3 gap-4 mb-6'>
                        <div className='bg-white border border-gray-200 rounded-lg p-4'>
                            <p className='text-xs font-semibold text-gray-400 uppercase tracking-wide mb-1'>Total Price</p>
                            <p className='text-xl font-bold text-gray-800'>
                                {cart.totalPrice != null ? `₹${Number(cart.totalPrice).toLocaleString()}` : '—'}
                            </p>
                        </div>
                        <div className='bg-white border border-gray-200 rounded-lg p-4'>
                            <p className='text-xs font-semibold text-gray-400 uppercase tracking-wide mb-1'>Discount</p>
                            <p className='text-xl font-bold text-green-600'>
                                {cart.totalDiscount != null ? `₹${Number(cart.totalDiscount).toLocaleString()}` : '—'}
                            </p>
                        </div>
                        <div className='bg-white border border-gray-200 rounded-lg p-4'>
                            <p className='text-xs font-semibold text-gray-400 uppercase tracking-wide mb-1'>Original Price</p>
                            <p className='text-xl font-bold text-gray-500 line-through'>
                                {cart.originalPrice != null ? `₹${Number(cart.originalPrice).toLocaleString()}` : '—'}
                            </p>
                        </div>
                    </div>
                )}

                {!checkedOut && (
                    <div className='bg-white border border-gray-200 rounded-lg p-4 mb-6'>
                        <h2 className='text-sm font-bold text-gray-700 mb-3'>Add Product</h2>
                        {addError && (
                            <div className='bg-red-50 text-red-600 text-sm px-3 py-2 rounded-lg mb-3 border border-red-200'>
                                {addError}
                            </div>
                        )}
                        <div className='flex gap-3 items-end'>
                            <div className='flex-1'>
                                <label className='text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1 block'>Product</label>
                                <select
                                    value={selectedProduct}
                                    onChange={(e) => setSelectedProduct(e.target.value)}
                                    className='border border-gray-300 rounded-lg px-3 py-2.5 text-sm outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-200 transition w-full'
                                >
                                    <option value=''>Select a product</option>
                                    {prices.map((p) => (
                                        <option key={p.identifier} value={p.product}>
                                            {p.product} — MRP ₹{p.mrp != null ? Number(p.mrp).toLocaleString() : '?'} / Price ₹{p.price != null ? Number(p.price).toLocaleString() : '?'}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className='w-28'>
                                <label className='text-xs font-semibold text-gray-500 uppercase tracking-wide mb-1 block'>Quantity</label>
                                <input
                                    type='number'
                                    min='1'
                                    value={quantity}
                                    onChange={(e) => setQuantity(e.target.value)}
                                    className='border border-gray-300 rounded-lg px-3 py-2.5 text-sm outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-200 transition w-full'
                                />
                            </div>
                            <button
                                onClick={handleAddEntry}
                                disabled={adding}
                                className='bg-blue-600 hover:bg-blue-700 text-white text-sm font-semibold py-2.5 px-4 rounded-lg transition disabled:opacity-50 whitespace-nowrap'
                            >
                                {adding ? 'Adding...' : '+ Add'}
                            </button>
                        </div>
                    </div>
                )}

                <div className='bg-white border border-gray-200 rounded-lg overflow-hidden'>
                    <div className='px-4 py-3 border-b border-gray-100'>
                        <h2 className='text-sm font-bold text-gray-700'>Cart Items ({entries.length})</h2>
                    </div>
                    {entries.length === 0 ? (
                        <div className='p-12 text-center'>
                            <p className='text-gray-400 text-sm'>No items in cart yet</p>
                        </div>
                    ) : (
                        <div className='overflow-x-auto'>
                            <table className='w-full text-sm'>
                                <thead>
                                    <tr className='bg-gray-50 border-b border-gray-200'>
                                        <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Product</th>
                                        <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Qty</th>
                                        <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>MRP</th>
                                        <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Price</th>
                                        <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Cost Price</th>
                                        <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Discount</th>
                                        <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Total</th>
                                        {!checkedOut && <th className='px-4 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wide'>Actions</th>}
                                    </tr>
                                </thead>
                                <tbody className='divide-y divide-gray-100'>
                                    {entries.map((entry) => (
                                        <tr key={entry.identifier} className='hover:bg-gray-50 transition'>
                                            <td className='px-4 py-3 text-gray-700 font-medium'>{entry.product}</td>
                                            <td className='px-4 py-3 text-gray-700'>{entry.quantity}</td>
                                            <td className='px-4 py-3 text-gray-500 line-through'>
                                                {entry.mrp != null ? `₹${Number(entry.mrp).toLocaleString()}` : '-'}
                                            </td>
                                            <td className='px-4 py-3 text-gray-700'>
                                                {entry.price != null ? `₹${Number(entry.price).toLocaleString()}` : '-'}
                                            </td>
                                            <td className='px-4 py-3 text-gray-700'>
                                                {entry.costPrice != null ? `₹${Number(entry.costPrice).toLocaleString()}` : '-'}
                                            </td>
                                            <td className='px-4 py-3 text-green-600'>
                                                {entry.discount != null ? `₹${Number(entry.discount).toLocaleString()}` : '-'}
                                            </td>
                                            <td className='px-4 py-3 text-gray-800 font-semibold'>
                                                {entry.totalPrice != null ? `₹${Number(entry.totalPrice).toLocaleString()}` : '-'}
                                            </td>
                                            {!checkedOut && (
                                                <td className='px-4 py-3'>
                                                    <button
                                                        onClick={() => handleRemoveEntry(entry.identifier)}
                                                        className='text-xs font-semibold text-red-500 hover:text-red-700 transition'
                                                    >
                                                        Remove
                                                    </button>
                                                </td>
                                            )}
                                        </tr>
                                    ))}
                                </tbody>
                                <tfoot>
                                    <tr className='bg-gray-50 border-t border-gray-200'>
                                        <td colSpan={6} className='px-4 py-3 text-xs font-semibold text-gray-500 uppercase tracking-wide text-right'>
                                            Grand Total
                                        </td>
                                        <td className='px-4 py-3 text-base font-bold text-gray-800'>
                                            {cart?.totalPrice != null ? `₹${Number(cart.totalPrice).toLocaleString()}` : '—'}
                                        </td>
                                        {!checkedOut && <td />}
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    )}
                </div>
            </div>
        </Layout>
    );
}

export default CartManage;