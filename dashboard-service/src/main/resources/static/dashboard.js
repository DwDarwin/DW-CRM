// Configuración
const API_URL = 'http://localhost:8080/api/dashboard';
let datosActuales = null;
let empresasLista = [];

// ============================================
// INICIALIZACIÓN
// ============================================
document.addEventListener('DOMContentLoaded', function() {
    cargarDashboard();

    // Recargar cada 30 segundos
    setInterval(cargarDashboard, 30000);
});

// ============================================
// CAMBIAR TABS
// ============================================
function mostrarTab(tabName) {
    // Ocultar todos los tabs
    const tabs = document.querySelectorAll('.content');
    tabs.forEach(tab => tab.classList.remove('active'));

    // Mostrar el tab seleccionado
    document.getElementById(tabName).classList.add('active');

    // Actualizar botones
    const botones = document.querySelectorAll('.nav-link-custom');
    botones.forEach(btn => btn.classList.remove('active'));
    event.target.closest('.nav-link-custom').classList.add('active');

    // Cargar datos si es necesario
    if (tabName === 'clientes') {
        cargarClientes();
    } else if (tabName === 'edicion') {
        llenarSelectsEdicion();
    }
}

// ============================================
// CARGAR DATOS DEL BACKEND
// ============================================
async function cargarDashboard() {
    try {
        actualizarEstado('Cargando...', 'conectado');

        const response = await fetch(API_URL);

        if (!response.ok) {
            throw new Error(`Error ${response.status}`);
        }

        const datos = await response.json();
        datosActuales = datos;

        // Actualizar estadísticas
        document.getElementById('totalEmpresas').textContent = datos.totalEmpresas;
        document.getElementById('totalContactos').textContent = datos.totalContactos;
        document.getElementById('totalInteracciones').textContent = datos.totalInteracciones;

        // Mostrar datos en dashboard
        mostrarDashboardData(datos.dashboard);

        // Llenar selects y filtros
        llenarSelectsEdicion();
        llenarFiltrosDashboard(datos.dashboard);
        llenarSelectsEliminacion(datos.dashboard);

        actualizarEstado('Conectado', 'conectado');

    } catch (error) {
        console.error('Error:', error);
        mostrarError(`Error al cargar: ${error.message}`);
        actualizarEstado('Desconectado', 'desconectado');
    }
}

// Llenar filtro del dashboard
function llenarFiltrosDashboard(dashboard) {
    const select = document.getElementById('filtroEmpresaDashboard');
    select.innerHTML = '<option value="">Todas las empresas</option>';

    dashboard.forEach(dash => {
        const option = document.createElement('option');
        option.value = dash.empresa.id;
        option.textContent = dash.empresa.nombre;
        select.appendChild(option);
    });
}

// Filtrar dashboard por empresa
function filtrarDashboardPorEmpresa() {
    const empresaId = document.getElementById('filtroEmpresaDashboard').value;

    if (!datosActuales) return;

    let filtrado = datosActuales.dashboard;

    if (empresaId) {
        filtrado = datosActuales.dashboard.filter(d => d.empresa.id == empresaId);
    }

    // Actualizar KPI según el filtro
    actualizarKPIsFiltrados(filtrado);

    // Mostrar datos
    mostrarDashboardData(filtrado);
}

// Actualizar KPI según filtro
function actualizarKPIsFiltrados(dashboard) {
    let totalEmpresas = 0;
    let totalContactos = 0;
    let totalInteracciones = 0;

    dashboard.forEach(emp => {
        totalEmpresas += 1;
        totalContactos += emp.contactos.length;

        emp.contactos.forEach(con => {
            totalInteracciones += con.interacciones.length;
        });
    });

    document.getElementById('totalEmpresas').textContent = totalEmpresas;
    document.getElementById('totalContactos').textContent = totalContactos;
    document.getElementById('totalInteracciones').textContent = totalInteracciones;
}

// ============================================
// MOSTRAR DASHBOARD
// ============================================
function mostrarDashboardData(dashboard) {
    const contenido = document.getElementById('dashboardData');

    if (!dashboard || dashboard.length === 0) {
        contenido.innerHTML = '<div class="text-center text-muted mt-5">No hay empresas registradas</div>';
        return;
    }

    let html = '';

    dashboard.forEach(dashEmpresa => {
        const empresa = dashEmpresa.empresa;
        const contactos = dashEmpresa.contactos;

        html += `
            <div class="empresa-section">
                <div class="empresa-titulo">
                    <i class="bi bi-building"></i> ${empresa.nombre}
                </div>

                <p class="text-muted mb-3">
                    <strong>Email:</strong> ${empresa.email || 'N/A'} |
                    <strong>Teléfono:</strong> ${empresa.telefono || 'N/A'}
                </p>
        `;

        if (contactos && contactos.length > 0) {
            html += '<div class="ms-3">';

            contactos.forEach(dashContacto => {
                const contacto = dashContacto.contacto;
                const interacciones = dashContacto.interacciones || [];

                html += `
                    <div class="contacto-item">
                        <h6 class="mb-1">
                            <i class="bi bi-person-circle"></i> ${contacto.nombre}
                        </h6>
                        <p class="text-muted small mb-2">
                            ${contacto.email || 'Sin email'} | ${contacto.telefono || 'Sin teléfono'}
                        </p>
                `;

                if (interacciones.length > 0) {
                    html += '<div class="mt-2"><strong>Interacciones:</strong>';
                    interacciones.forEach(inter => {
                        html += `
                            <div class="interaccion-item">
                                <strong>${inter.tipo}</strong> - ${inter.fecha}
                                <br>
                                <small>${inter.resultado || 'Sin resultado'}</small>
                            </div>
                        `;
                    });
                    html += '</div>';
                } else {
                    html += '<p class="text-muted small mt-2">Sin interacciones</p>';
                }

                html += '</div>';
            });

            html += '</div>';
        } else {
            html += '<p class="text-muted">No hay contactos registrados</p>';
        }

        html += '</div>';
    });

    contenido.innerHTML = html;
}

// ============================================
// CARGAR CLIENTES (EMPRESAS)
// ============================================
async function cargarClientes() {
    try {
        const response = await fetch(`${API_URL}/empresas`);

        if (!response.ok) throw new Error('Error al cargar');

        const empresas = await response.json();
        empresasLista = empresas;

        mostrarClientes(empresas);

    } catch (error) {
        console.error('Error:', error);
        mostrarError(error.message);
    }
}

function mostrarClientes(empresas) {
    const tbody = document.getElementById('tbodyClientes');

    if (!empresas || empresas.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center text-muted">No hay empresas</td></tr>';
        return;
    }

    tbody.innerHTML = empresas.map(empresa => {
        // Contar contactos de esta empresa
        const dashEmpresa = datosActuales.dashboard.find(d => d.empresa.id === empresa.id);
        const cantidadContactos = dashEmpresa ? dashEmpresa.contactos.length : 0;

        return `
        <tr>
            <td>${empresa.id}</td>
            <td><strong>${empresa.nombre}</strong></td>
            <td>${empresa.email || '-'}</td>
            <td>${empresa.telefono || '-'}</td>
            <td style="text-align: center;">
                <span class="badge bg-info">${cantidadContactos}</span>
            </td>
        </tr>
    `;
    }).join('');
}

function filtrarClientes() {
    const valor = document.getElementById('filtroClientes').value.toLowerCase();
    const filas = document.querySelectorAll('#tbodyClientes tr');

    filas.forEach(fila => {
        const texto = fila.textContent.toLowerCase();
        fila.style.display = texto.includes(valor) ? '' : 'none';
    });
}

// ============================================
// LLENAR SELECTS DE EDICIÓN Y ELIMINACIÓN
// ============================================
function llenarSelectsEdicion() {
    if (!datosActuales) return;

    const selectEmpresa = document.getElementById('edEmpresaContacto');
    const selectEmpresaInteraccion = document.getElementById('edEmpresaInteraccionNew');

    selectEmpresa.innerHTML = '<option value="">Selecciona una empresa</option>';
    selectEmpresaInteraccion.innerHTML = '<option value="">Selecciona una empresa</option>';

    datosActuales.dashboard.forEach(dash => {
        const empresa = dash.empresa;

        // Para crear contacto
        const option = document.createElement('option');
        option.value = empresa.id;
        option.textContent = empresa.nombre;
        selectEmpresa.appendChild(option);

        // Para crear interacción
        const option2 = document.createElement('option');
        option2.value = empresa.id;
        option2.textContent = empresa.nombre;
        selectEmpresaInteraccion.appendChild(option2);
    });
}
    // Actualizar contactos cuando cambia empresa en Nueva Interacción
    function actualizarContactosInteraccionNew() {
        const empresaId = document.getElementById('edEmpresaInteraccionNew').value;
        const selectContacto = document.getElementById('edContactoInteraccion');

        selectContacto.innerHTML = '<option value="">Selecciona un contacto</option>';

        if (!empresaId || !datosActuales) return;

        const empresa = datosActuales.dashboard.find(d => d.empresa.id == empresaId);

        if (empresa) {
            empresa.contactos.forEach(dashContacto => {
                const option = document.createElement('option');
                option.value = dashContacto.contacto.id;
                option.textContent = dashContacto.contacto.nombre;
                selectContacto.appendChild(option);
            });
        }
    }


// Llenar selects de eliminación
function llenarSelectsEliminacion(dashboard) {
    // Llenar empresas para eliminar empresa
    const selectEmpresaEliminar = document.getElementById('eliminarEmpresaSelect');
    selectEmpresaEliminar.innerHTML = '<option value="">Selecciona una empresa</option>';

    // Llenar empresas para contacto
    const selectEmpresaContacto = document.getElementById('eliminarEmpresaContactoSelect');
    selectEmpresaContacto.innerHTML = '<option value="">Selecciona una empresa</option>';

    // Llenar empresas para interacción
    const selectEmpresaInteraccion = document.getElementById('eliminarEmpresaInteraccionSelect');
    selectEmpresaInteraccion.innerHTML = '<option value="">Selecciona una empresa</option>';

    // Llenar todos los selects de empresa
    dashboard.forEach(dash => {
        const option1 = document.createElement('option');
        option1.value = dash.empresa.id;
        option1.textContent = dash.empresa.nombre;
        selectEmpresaEliminar.appendChild(option1);

        const option2 = document.createElement('option');
        option2.value = dash.empresa.id;
        option2.textContent = dash.empresa.nombre;
        selectEmpresaContacto.appendChild(option2);

        const option3 = document.createElement('option');
        option3.value = dash.empresa.id;
        option3.textContent = dash.empresa.nombre;
        selectEmpresaInteraccion.appendChild(option3);
    });

    // Limpiar selects dependientes
    document.getElementById('eliminarContactoSelect').innerHTML = '<option value="">Selecciona un contacto</option>';
    document.getElementById('eliminarContactoInteraccionSelect').innerHTML = '<option value="">Selecciona un contacto</option>';
    document.getElementById('eliminarInteraccionSelect').innerHTML = '<option value="">Selecciona una interacción</option>';
}

// Actualizar contactos cuando cambia la empresa (para eliminar contacto)
function actualizarContactosEliminacion() {
    const empresaId = document.getElementById('eliminarEmpresaContactoSelect').value;
    const selectContacto = document.getElementById('eliminarContactoSelect');

    selectContacto.innerHTML = '<option value="">Selecciona un contacto</option>';

    if (!empresaId || !datosActuales) return;

    const empresa = datosActuales.dashboard.find(d => d.empresa.id == empresaId);

    if (empresa) {
        empresa.contactos.forEach(dashContacto => {
            const option = document.createElement('option');
            option.value = dashContacto.contacto.id;
            option.textContent = dashContacto.contacto.nombre;
            selectContacto.appendChild(option);
        });
    }
}

// Actualizar contactos cuando cambia la empresa (para eliminar interacción)
function actualizarContactosInteraccionEliminacion() {
    const empresaId = document.getElementById('eliminarEmpresaInteraccionSelect').value;
    const selectContacto = document.getElementById('eliminarContactoInteraccionSelect');

    selectContacto.innerHTML = '<option value="">Selecciona un contacto</option>';
    document.getElementById('eliminarInteraccionSelect').innerHTML = '<option value="">Selecciona una interacción</option>';

    if (!empresaId || !datosActuales) return;

    const empresa = datosActuales.dashboard.find(d => d.empresa.id == empresaId);

    if (empresa) {
        empresa.contactos.forEach(dashContacto => {
            const option = document.createElement('option');
            option.value = dashContacto.contacto.id;
            option.textContent = dashContacto.contacto.nombre;
            selectContacto.appendChild(option);
        });
    }
}

// Actualizar interacciones cuando cambia el contacto
function actualizarInteraccionesEliminacion() {
    const empresaId = document.getElementById('eliminarEmpresaInteraccionSelect').value;
    const contactoId = document.getElementById('eliminarContactoInteraccionSelect').value;
    const selectInteraccion = document.getElementById('eliminarInteraccionSelect');

    selectInteraccion.innerHTML = '<option value="">Selecciona una interacción</option>';

    if (!empresaId || !contactoId || !datosActuales) return;

    const empresa = datosActuales.dashboard.find(d => d.empresa.id == empresaId);

    if (empresa) {
        const contacto = empresa.contactos.find(c => c.contacto.id == contactoId);

        if (contacto) {
            contacto.interacciones.forEach(inter => {
                const option = document.createElement('option');
                option.value = inter.id;
                option.textContent = `${inter.tipo} - ${inter.fecha}`;
                selectInteraccion.appendChild(option);
            });
        }
    }
}

// ============================================
// GUARDAR DATOS
// ============================================
async function guardarEmpresaEdicion() {
    const nombre = document.getElementById('edNombreEmpresa').value;
    const email = document.getElementById('edEmailEmpresa').value;
    const telefono = document.getElementById('edTelefonoEmpresa').value;

    if (!nombre) {
        mostrarError('El nombre es obligatorio');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/empresas`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nombre, email, telefono })
        });

        if (!response.ok) throw new Error('Error al guardar');

        document.getElementById('formEmpresa').reset();
        mostrarExito('Empresa creada exitosamente');
        cargarDashboard();

    } catch (error) {
        mostrarError(error.message);
    }
}

async function guardarContactoEdicion() {
    const empresaId = document.getElementById('edEmpresaContacto').value;
    const nombre = document.getElementById('edNombreContacto').value;
    const email = document.getElementById('edEmailContacto').value;
    const telefono = document.getElementById('edTelefonoContacto').value;

    if (!empresaId || !nombre) {
        mostrarError('Completa los campos obligatorios');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/contactos`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ empresaId, nombre, email, telefono })
        });

        if (!response.ok) throw new Error('Error al guardar');

        document.getElementById('formContacto').reset();
        mostrarExito('Contacto creado exitosamente');
        cargarDashboard();

    } catch (error) {
        mostrarError(error.message);
    }
}

async function guardarInteraccionEdicion() {
    const contactoId = document.getElementById('edContactoInteraccion').value;
    const tipo = document.getElementById('edTipoInteraccion').value;
    const fecha = document.getElementById('edFechaInteraccion').value;
    const resultado = document.getElementById('edResultadoInteraccion').value;

    console.log('Datos a enviar:', { contactoId, tipo, fecha, resultado }); // ← AGREGA ESTA LÍNEA

    if (!contactoId || !tipo || !fecha) {
        mostrarError('Completa los campos obligatorios');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/interacciones`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                contactoId,
                tipo,
                fecha,
                resultado
            })
        });

        if (!response.ok) throw new Error('Error al guardar');

        document.getElementById('formInteraccion').reset();
        mostrarExito('Interacción registrada exitosamente');
        cargarDashboard();

    } catch (error) {
        mostrarError(error.message);
    }
}

// ============================================
// ELIMINAR DATOS
// ============================================
async function eliminarEmpresa(id) {
    if (!confirm('¿Está seguro de eliminar esta empresa?')) return;

    try {
        const response = await fetch(`${API_URL}/empresas/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Error al eliminar');

        mostrarExito('Empresa eliminada');
        cargarDashboard();

    } catch (error) {
        mostrarError(error.message);
    }
}

async function eliminarContacto(id) {
    if (!confirm('¿Está seguro de eliminar este contacto?')) return;

    try {
        const response = await fetch(`${API_URL}/contactos/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Error al eliminar');

        mostrarExito('Contacto eliminado');
        cargarDashboard();

    } catch (error) {
        mostrarError(error.message);
    }
}

async function eliminarInteraccion(id) {
    if (!confirm('¿Está seguro de eliminar esta interacción?')) return;

    try {
        const response = await fetch(`${API_URL}/interacciones/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Error al eliminar');

        mostrarExito('Interacción eliminada');
        cargarDashboard();

    } catch (error) {
        mostrarError(error.message);
    }
}

// ============================================
// FUNCIONES AUXILIARES
// ============================================
function mostrarError(mensaje) {
    mostrarAlerta(mensaje, 'danger');
}

function mostrarExito(mensaje) {
    mostrarAlerta(mensaje, 'success');
}

function mostrarAlerta(mensaje, tipo) {
    const alertas = document.getElementById('alertas');
    const alert = document.createElement('div');
    alert.className = `alert alert-${tipo} alert-dismissible fade show`;
    alert.innerHTML = `
        ${mensaje}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    alertas.appendChild(alert);

    setTimeout(() => alert.remove(), 5000);
}

function actualizarEstado(texto, tipo) {
    const estado = document.getElementById('estado');
    const clase = tipo === 'conectado' ? 'estado-conectado' : 'estado-desconectado';
    const icono = tipo === 'conectado' ? 'bi-check-circle' : 'bi-exclamation-circle';
    estado.className = `estado-badge ${clase}`;
    estado.innerHTML = `<i class="bi ${icono}"></i> ${texto}`;
}

// ============================================
// FUNCIONES DE ELIMINACIÓN DEL PANEL
// ============================================
async function eliminarEmpresaPanel() {
    const id = document.getElementById('eliminarEmpresaSelect').value;

    if (!id) {
        mostrarError('Selecciona una empresa');
        return;
    }

    if (!confirm('¿Está seguro de eliminar esta empresa?')) return;

    try {
        const response = await fetch(`${API_URL}/empresas/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Error al eliminar');

        document.getElementById('eliminarEmpresaSelect').value = '';
        mostrarExito('Empresa eliminada');
        cargarDashboard();

    } catch (error) {
        mostrarError(error.message);
    }
}

async function eliminarContactoPanel() {
    const id = document.getElementById('eliminarContactoSelect').value;

    if (!id) {
        mostrarError('Selecciona un contacto');
        return;
    }

    if (!confirm('¿Está seguro de eliminar este contacto?')) return;

    try {
        const response = await fetch(`${API_URL}/contactos/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Error al eliminar');

        document.getElementById('eliminarContactoSelect').value = '';
        mostrarExito('Contacto eliminado');
        cargarDashboard();

    } catch (error) {
        mostrarError(error.message);
    }
}

async function eliminarInteraccionPanel() {
    const id = document.getElementById('eliminarInteraccionSelect').value;

    if (!id) {
        mostrarError('Selecciona una interacción');
        return;
    }

    if (!confirm('¿Está seguro de eliminar esta interacción?')) return;

    try {
        const response = await fetch(`${API_URL}/interacciones/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) throw new Error('Error al eliminar');

        document.getElementById('eliminarInteraccionSelect').value = '';
        mostrarExito('Interacción eliminada');
        cargarDashboard();

    } catch (error) {
        mostrarError(error.message);
    }
}