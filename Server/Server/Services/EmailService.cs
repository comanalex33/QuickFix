using System;
using MailKit.Net.Smtp;
using MailKit.Security;
using Microsoft.Extensions.Configuration;
using MimeKit;
using MimeKit.Text;

namespace Server.Services
{
    public interface IEmailService
    {
        void Send(string to, string subject, string html, string from = null);
    }
    public class EmailService : IEmailService
    {
        private readonly IConfiguration _configuration;
        public EmailService(IConfiguration configuration)
        {
            _configuration = configuration;
        }
        public void Send(string to, string subject, string html, string from = null)
        {
            var email = new MimeMessage();
            email.From.Add(MailboxAddress.Parse(from ?? _configuration["Email:EmailFrom"]));
            email.To.Add(MailboxAddress.Parse(to));
            email.Subject = subject;
            email.Body = new TextPart(TextFormat.Html) { Text = html };

            using var smtp = new SmtpClient();
            smtp.Connect(_configuration["Email:SmtpHost"], Int32.Parse(_configuration["Email:SmtpPort"]), SecureSocketOptions.StartTls);
            smtp.Authenticate(_configuration["Email:SmtpUser"], _configuration["Email:SmtpPass"]);
            smtp.Send(email);
            smtp.Disconnect(true);
        }
    }
}
