type AuthFieldProps = {
    icon: React.ComponentType<{className?: string}>,
    fieldLabel: string,
    fieldType: string,
    fieldPlaceholder: string,
    value: string,
    onChange: (value: string) => void
    extra?: React.ReactNode
}

export default function AuthField({
    icon: Icon,
    fieldLabel,
    fieldType,
    fieldPlaceholder,
    value,
    onChange,
    extra
}: AuthFieldProps) {
    return (
        <>
            <div className="field-group">
                { extra ? (
                    <div className="field-group-row">
                        <label className="field-label">Password</label>
                        {extra}
                    </div>
                ) : (
                <label className="field-label">{fieldLabel}</label>
                )}
                <div className="field-input-wrapper">
                    <Icon className="field-icon" />
                    <input className="field-input" type={fieldType} placeholder={fieldPlaceholder} value={value} onChange={(e) => onChange(e.target.value)} required />
                </div>
            </div>
        </>
    )
}