function createAjaxData(e) {
    var t, n, a = [];
    for (t in e) e.hasOwnProperty(t) && "sign" !== t && (a[a.length] = [t, e[t]]);
    a.sort(function(e, t) {
        return e[0] === t[0] ? 0 : e[0] > t[0] ? 1 : -1
    });
    var r = "";
    for (t = 0; t < a.length; t++) r += a[t][0] + a[t][1];
    var o = [5, 5, 27, 35, 0, 3, 27, 29, 27, 33, 3, 29, 32, 35, 4, 2, 33, 30, 27, 3, 4, 32, 32, 33, 35, 5, 5, 3, 2, 34, 33, 2],
    i = "";
    for (t = 0, n = o.length; t < n; t++) i += "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".substr(o[t], 1);
    var s = SYUtils.enc.md5.encode(r),
    l = SYUtils.enc.des.encode(i, s);
    return e.sign = SYUtils.enc.md5.encode(r + l),
    e
} 