import React, { useState } from 'react';
import { Container, Row, Col, Card, Form } from 'react-bootstrap';
import './Dashboard.scss';

const Dashboard = ({ isSidebarOpen }) => {
    const [isAuto, setIsAuto] = useState(false);

    const sensorData = [
        { 
            id: 1, 
            title: 'TEMPERATURE', 
            value: '28°C', 
            icon: 'fa-thermometer-half', 
            color: '#ff4757' 
        },
        { 
            id: 2, 
            title: 'SOIL MOISTURE', 
            value: '65%', 
            icon: 'fa-tint', 
            color: '#133a94' 
        },
        { 
            id: 3, 
            title: 'LIGHT INTENSITY', 
            value: '800 Lux', 
            icon: 'fa-sun-o', 
            color: '#ffa502' 
        },
        { 
            id: 4, 
            title: 'PUMP STATUS', 
            value: isAuto ? 'Auto Running' : 'Off', 
            icon: 'fa-shower', 
            color: '#2ed573' 
        },
    ];

    return (
        <div className={`dashboard-wrapper ${isSidebarOpen ? 'sidebar-open' : 'sidebar-closed'}`}>
            <Container fluid className="py-4">
                <div className="d-flex justify-content-between align-items-center mb-4 px-3">
                    <h4 className="fw-bold text-secondary">Dashboard</h4>
                    <div className="mode-toggle d-flex align-items-center bg-white p-2 rounded-pill shadow-sm">
                        <span className={`me-2 fw-bold small ${!isAuto ? 'text-primary' : 'text-muted'}`}>
                            Manual
                        </span>
                        <Form.Check 
                            type="switch"
                            id="mode-switch"
                            checked={isAuto}
                            onChange={() => setIsAuto(!isAuto)}
                            className="custom-switch"
                        />
                        <span className={`ms-1 fw-bold small ${isAuto ? 'text-success' : 'text-muted'}`}>
                            Auto
                        </span>
                    </div>
                </div>
                <Row className="g-4">
                    {sensorData.map((item) => (
                        <Col key={item.id} xs={12} md={6} xl={3}>
                            <Card className="sensor-card border-0 shadow-sm h-100">
                                <Card.Body className="d-flex align-items-center">
                                    <div 
                                        className="icon-box" 
                                        style={{ backgroundColor: `${item.color}15`, color: item.color }}
                                    >
                                        <i className={`fa ${item.icon}`}></i>
                                    </div>
                                    <div className="ms-3">
                                        <p className="text-muted mb-0 small fw-bold text-uppercase">
                                            {item.title}
                                        </p>
                                        <h3 className="mb-0 fw-bold">{item.value}</h3>
                                    </div>
                                </Card.Body>
                            </Card>
                        </Col>
                    ))}
                </Row>
                <Row className="mt-4">
                    <Col lg={6} xl={5}>
                        <Card className="border-0 shadow-sm p-4 control-card">
                            <h5 className="fw-bold mb-4">Device Control</h5>
                            <div className="d-flex justify-content-between align-items-center">
                                <div className="d-flex align-items-center">
                                    <div className="pump-icon-bg me-3">
                                        <i className="fa fa-tint text-primary"></i>
                                    </div>
                                    <span className="fw-medium text-secondary">Water Pump</span>
                                </div>
                                <button 
                                    className={`btn btn-lg rounded-pill px-4 fw-bold shadow-sm transition-all 
                                        ${isAuto ? 'btn-light text-muted cursor-not-allowed' : 'btn-primary'}`}
                                    disabled={isAuto}
                                >
                                    {isAuto ? 'System Automatic' : 'Pump Now'}
                                </button>
                            </div>
                            {isAuto && (
                                <div className="mt-3 p-2 bg-light rounded text-center">
                                    <small className="text-primary italic">
                                        <i className="fa fa-info-circle me-1"></i>
                                        Auto mode is on. 
                                    </small>
                                </div>
                            )}
                        </Card>
                    </Col>
                </Row>
            </Container>
        </div>
    );
};

export default Dashboard;